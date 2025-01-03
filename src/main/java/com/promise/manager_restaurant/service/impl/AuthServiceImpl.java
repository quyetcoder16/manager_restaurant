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
import com.promise.manager_restaurant.dto.response.auth.VerifyEmailResponse;
import com.promise.manager_restaurant.dto.response.email.DataMailDTO;
import com.promise.manager_restaurant.entity.*;
import com.promise.manager_restaurant.entity.keys.KeyUserRoleId;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.UserMapper;
import com.promise.manager_restaurant.repository.*;
import com.promise.manager_restaurant.service.AuthService;
import com.promise.manager_restaurant.service.MailService;
import com.promise.manager_restaurant.utils.Const;
import com.promise.manager_restaurant.utils.OTPGenerator;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    // Các repository và mapper để tương tác với cơ sở dữ liệu
    UserRepository userRepository;
    UserMapper userMapper;
    InvalidatedTokenRepository invalidatedTokenRepository;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;
    MailService mailService;
    OtpVerificationRepository otpVerificationRepository;

    // Các giá trị được cấu hình từ tệp cấu hình ứng dụng
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

    /**
     * Đăng ký người dùng mới.
     *
     * @param registerRequest Thông tin đăng ký từ client.
     * @return Thông tin phản hồi sau khi đăng ký thành công.
     */
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws MessagingException {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (userRepository.existsUserByPhone(registerRequest.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        Role role = roleRepository.findRoleByRoleName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        // Chuyển đổi từ DTO sang entity và mã hóa mật khẩu
        User user = userMapper.toUser(registerRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setIsActive(true);
        user.setIsVerifyEmail(false);
        User savedUser = userRepository.save(user);
        KeyUserRoleId keyUserRoleId = new KeyUserRoleId();
        keyUserRoleId.setRoleName(role.getRoleName());
        keyUserRoleId.setUserId(savedUser.getUserId());

        UserRole userRole = UserRole.builder()
                .keyUserRoleId(keyUserRoleId)
                .build();

        userRoleRepository.save(userRole);

        String otp = OTPGenerator.generateOTP();

        Map<String, Object> props = new HashMap<>();
        props.put("email", user.getEmail());
        props.put("firstName", user.getFirstName());
        props.put("lastName", user.getLastName());
        props.put("otp", otp);

        OtpVerification otpVerification = OtpVerification.builder()
                .email(user.getEmail())
                .otp(otp)
                .isUsed(false)
                .build();
        otpVerificationRepository.save(otpVerification);

        DataMailDTO dataMailDTO = DataMailDTO.builder()
                .to(user.getEmail())
                .subject(Const.SEND_MAIL_SUBJECT.OTP_VERIFY)
                .props(props)
                .build();

        mailService.send(dataMailDTO, Const.TEMPLATE_FILE_NAME.OTP_VERIFY);

        // Lưu người dùng mới vào cơ sở dữ liệu
        return userMapper.toRegisterResponse(savedUser);
    }

    /**
     * Xử lý đăng nhập người dùng.
     *
     * @param authenticationRequest Thông tin đăng nhập từ client.
     * @return Access Token và Refresh Token nếu đăng nhập thành công.
     */
    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        // Tìm người dùng theo email
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_EXISTED); // Ném lỗi nếu không tìm thấy
        });

        if (user.getIsVerifyEmail() == null || !user.getIsVerifyEmail()) {
            throw new AppException(ErrorCode.NOT_VERIFY_EMAIL);
        }


        if (user.getIsActive() == null || !user.getIsActive()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }

        // Xác thực mật khẩu
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED); // Ném lỗi nếu mật khẩu không đúng
        }

        // Tạo Access Token và Refresh Token
        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        // Trả về phản hồi xác thực
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Xác thực token.
     *
     * @param introspectRequest Yêu cầu introspect từ client.
     * @return Trạng thái hợp lệ của token.
     */
    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var token = introspectRequest.getAccessToken();
        boolean isValid = true;


        SignedJWT jwt = verifyToken(token, false); // Kiểm tra token
        String userId = jwt.getJWTClaimsSet().getSubject();

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!user.getIsActive()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }


        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    /**
     * Xử lý làm mới token.
     *
     * @param refreshRequest Yêu cầu làm mới từ client.
     * @return Access Token và Refresh Token mới.
     */
    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) throws ParseException, JOSEException {
        var signedJWT = verifyToken(refreshRequest.getRefreshToken(), true);
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Lưu token đã bị vô hiệu hóa nếu hết hạn
        if (expiryTime.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }

        var userId = signedJWT.getJWTClaimsSet().getSubject();

        // Tìm người dùng theo user Id
        var user = userRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        if (!user.getIsActive()) {
            throw new AppException(ErrorCode.USER_LOCKED);
        }

        // Tạo Access Token và Refresh Token mới
        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Xử lý đăng xuất người dùng.
     *
     * @param logoutRequest Yêu cầu đăng xuất từ client.
     */
    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            // Vô hiệu hóa Refresh Token
            var signedToken = verifyToken(logoutRequest.getRefresh_token(), true);
            String jit = signedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException appException) {
            log.info("Refresh token already expired");
        }

        try {
            // Vô hiệu hóa Access Token
            var signedToken = verifyToken(logoutRequest.getAccess_token(), false);
            String jit = signedToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException appException) {
            log.info("Access token already expired");
        }
    }

    @Override
    public VerifyEmailResponse verifyEmail(VerifyEmailRequest verifyEmailRequest) {
        boolean isVerifyEmail = false;

        OtpVerification otpVerification = otpVerificationRepository.findByEmail(verifyEmailRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOND));

        if (otpVerification.getIsUsed()) {
            throw new AppException(ErrorCode.OTP_IS_USED);
        }

        if (otpVerification.getOtp().equals(verifyEmailRequest.getOtp())) {

            User user = userRepository.findByEmail(verifyEmailRequest.getEmail()).orElseThrow(
                    () -> new AppException(ErrorCode.EMAIL_NOT_FOND)
            );

            user.setIsVerifyEmail(true);
            userRepository.save(user);

            isVerifyEmail = true;
            otpVerification.setIsUsed(true);
            otpVerificationRepository.save(otpVerification);
        }


        return VerifyEmailResponse.builder()
                .success(isVerifyEmail)
                .build();
    }

    /**
     * Kiểm tra tính hợp lệ của token.
     *
     * @param token     Token cần xác thực.
     * @param isRefresh Xác định token là Refresh Token hay Access Token.
     * @return Token JWT đã được xác thực.
     */
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        String signerKey = isRefresh ? REFRESH_TOKEN_SIGNER_KEY : ACCESS_TOKEN_SIGNER_KEY;
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Kiểm tra thời gian hết hạn và tính hợp lệ của chữ ký
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiration.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Kiểm tra xem token có bị vô hiệu hóa không
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

        // Kiểm tra null và danh sách user roles
        List<UserRole> userRoles = user.getListUserRole();
        if (CollectionUtils.isEmpty(userRoles)) {
            return scopeJoiner.toString(); // Trả về chuỗi rỗng nếu không có user roles
        }

        userRoles.forEach(userRole -> {
            Role role = userRole.getRole();
            if (role == null) {
                return; // Bỏ qua nếu role là null
            }

            scopeJoiner.add("ROLE_" + role.getRoleName()); // Thêm vai trò vào scope

            // Kiểm tra null và danh sách role permissions
            List<RolePermission> rolePermissions = role.getListRolePermission();
            if (!CollectionUtils.isEmpty(rolePermissions)) {
                rolePermissions.forEach(rolePermission -> {
                    Permission permission = rolePermission.getPermission();
                    if (permission != null) {
                        scopeJoiner.add(permission.getPermissionName()); // Thêm quyền vào scope
                    }
                });
            }
        });

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
                .subject(user.getUserId())
                .issuer("com.promise.manager_restaurant")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("email", user.getEmail())
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
                .subject(user.getUserId())
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
