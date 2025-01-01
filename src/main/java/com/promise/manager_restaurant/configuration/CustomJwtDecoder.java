package com.promise.manager_restaurant.configuration;


import com.nimbusds.jose.JOSEException;
import com.promise.manager_restaurant.dto.request.auth.IntrospectRequest;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

/**
 * CustomJwtDecoder là một lớp tùy chỉnh dùng để giải mã (decode) và xác minh token JWT.
 * Nó được cài đặt giao diện JwtDecoder của Spring Security.
 */
@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    // Key dùng để ký JWT, được cấu hình trong tệp `application.properties`.
    @Value("${jwt.access-token-signer-key}")
    private String accessTokenSignerKey;

    // Dịch vụ xác thực được sử dụng để kiểm tra tính hợp lệ của token với máy chủ từ xa.
    @Autowired
    private AuthService authService;

    // Decoder NimbusJwtDecoder được sử dụng để thực hiện việc decode token sau khi xác minh.
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    /**
     * Phương thức chính để giải mã và xác minh token JWT.
     *
     * @param token JWT token cần decode.
     * @return Đối tượng Jwt nếu token hợp lệ.
     * @throws JwtException nếu token không hợp lệ hoặc không thể decode.
     */
    @Override
    public Jwt decode(String token) throws JwtException {
        // Bước 1: Kiểm tra token với máy chủ xác thực từ xa
        try {
            var response = authService.introspect(IntrospectRequest.builder()
                    .accessToken(token)
                    .build());

            if (!response.isValid()) {
                throw new JwtException("INVALID_TOKEN"); // Ném lỗi cụ thể
            }
        } catch (AppException appException) {
            throw new AuthenticationServiceException(appException.getErrorCode().name(), appException);
        } catch (JOSEException | ParseException e) {
            throw new JwtException("TOKEN_VERIFICATION_FAILED");
        }
//        } catch (JwtException jwtException) {
//            // Propagate JwtException wrapped in AuthenticationException
//            throw new AuthenticationServiceException("JWT processing error", jwtException);
//        }

        // Bước 2: Khởi tạo NimbusJwtDecoder nếu chưa được khởi tạo
        if (Objects.isNull(nimbusJwtDecoder)) {
            // Tạo SecretKeySpec từ khóa ký JWT và thuật toán HS256
            SecretKeySpec secretKeySpec = new SecretKeySpec(accessTokenSignerKey.getBytes(), "HS256");

            // Khởi tạo NimbusJwtDecoder với secret key và thuật toán ký HS256
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256) // Xác định thuật toán MAC là HS256
                    .build();
        }


        // Bước 3: Giải mã token bằng NimbusJwtDecoder
        return nimbusJwtDecoder.decode(token);

    }
}
