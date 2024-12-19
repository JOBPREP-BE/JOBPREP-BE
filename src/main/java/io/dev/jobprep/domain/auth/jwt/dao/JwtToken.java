package io.dev.jobprep.domain.auth.jwt.dao;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtToken {

    private Long userId;
    private String grantType; //JWT에 대한 인증 타입
    private String accessToken;// 클라이언트는 전달받은 액세스 토큰을 헤더에 담아 사용
    private String refreshToken;// Access token을 발급받기 위한 토큰

    public static JwtToken of(Long userId, String grantType, String accessToken, String refreshToken) {
        return new JwtToken(userId, grantType, accessToken, refreshToken);
    }

    public static JwtToken of(Long userId, String grantType, String accessToken) {
        return new JwtToken(userId, grantType, accessToken, null);
    }
}
