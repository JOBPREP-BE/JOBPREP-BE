package io.dev.jobprep.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TokenInfo {
    private String grantType; //JWT에 대한 인증 타입
    private String accessToken;// 클라이언트는 전달받은 액세스 토큰을 헤더에 담아 사용
    private String refreshToken;// Access token을 발급받기 위한 토큰
}