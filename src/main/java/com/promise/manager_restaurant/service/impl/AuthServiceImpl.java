package com.promise.manager_restaurant.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.promise.manager_restaurant.dto.request.auth.*;
import com.promise.manager_restaurant.dto.response.auth.AuthenticationResponse;
import com.promise.manager_restaurant.dto.response.auth.IntrospectResponse;
import com.promise.manager_restaurant.dto.response.auth.RegisterResponse;
import com.promise.manager_restaurant.entity.InvalidatedToken;
import com.promise.manager_restaurant.entity.Permission;
import com.promise.manager_restaurant.entity.Role;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.UserMapper;
import com.promise.manager_restaurant.repository.InvalidatedTokenRepository;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.access-token-signer-key}")
    private String ACCESS_TOKEN_SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refresh-token-signer-key}")
    private String REFRESH_TOKEN_SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected Long REFRESHABLE_DURATION;


    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(registerRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);

        return userMapper.toRegisterResponse(userRepository.save(user));
    }


    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        // Tìm người dùng theo email từ cơ sở dữ liệu.
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_EXISTED); // Ném lỗi nếu không tìm thấy người dùng.
        });


        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED); // Ném lỗi nếu mật khẩu không khớp.
        }

        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var token = introspectRequest.getAccessToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) throws ParseException, JOSEException {
        var signedJWT = verifyToken(refreshRequest.getRefreshToken(), true);
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (expiryTime.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }

        var email = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            var signedToken = verifyToken(logoutRequest.getRefresh_token(), true);
            String jit = signedToken.getJWTClaimsSet().getJWTID(); // Lấy ID của token.
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime(); // Lấy thời gian hết hạn.

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException appException) {
            log.info("token already expired");
        }

        try {
            var signedToken = verifyToken(logoutRequest.getAccess_token(), false);
            String jit = signedToken.getJWTClaimsSet().getJWTID(); // Lấy ID của token.
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime(); // Lấy thời gian hết hạn.

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException appException) {
            log.info("token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        String signerKey = isRefresh ? REFRESH_TOKEN_SIGNER_KEY : ACCESS_TOKEN_SIGNER_KEY;
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiration.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return signedJWT;
    }


    /**
     * Xây dựng thông tin quyền hạn (scope) của người dùng.
     *
     * @param user Thông tin người dùng.
     * @return Chuỗi scope chứa các quyền và vai trò của người dùng.
     */
    private String buildScope(User user) {
        StringJoiner scopeJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getListUserRole())) {
            user.getListUserRole().forEach(userRole -> {
                Role role = userRole.getRole();
                scopeJoiner.add("ROLE_" + role.getRoleName()); // Thêm vai trò vào scope.
                if (!CollectionUtils.isEmpty(role.getListRolePermission())) {
                    role.getListRolePermission().forEach(rolePermission -> {
                        Permission permission = rolePermission.getPermission();
                        scopeJoiner.add(permission.getPermissionName()); // Thêm quyền vào scope.
                    });
                }
            });
        }
        return scopeJoiner.toString();
    }

    /**
     * Tạo Access Token JWT.
     *
     * @param user Thông tin người dùng.
     * @return Chuỗi Access Token JWT.
     */
    private String generateAccessToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("com.promise.manager_restaurant")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getUserId())
                .claim("scope", buildScope(user))
                .build();

        try {
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            signedJWT.sign(new MACSigner(ACCESS_TOKEN_SIGNER_KEY.getBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Can't create Access Token", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Tạo Refresh Token JWT.
     *
     * @param user Thông tin người dùng.
     * @return Chuỗi Refresh Token JWT.
     */
    private String generateRefreshToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("com.promise.manager_restaurant")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        try {
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            signedJWT.sign(new MACSigner(REFRESH_TOKEN_SIGNER_KEY.getBytes()));
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Can't create Refresh Token", e);
            throw new RuntimeException(e);
        }
    }
}
