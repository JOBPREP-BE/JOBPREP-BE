package io.dev.jobprep.domain.security.oauth.presentation;


import io.dev.jobprep.domain.security.jwt.application.AuthService;
import io.dev.jobprep.domain.security.jwt.application.JwtRedisService;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.jwt.exception.TokenCachingException;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.common.swagger.template.OauthSwagger;
import io.dev.jobprep.domain.security.oauth.presentation.dto.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OauthController implements OauthSwagger{

    private final JwtService jwtService;
    private final JwtRedisService jwtRedisService;
    private final AuthService authService;

    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidity;
    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidity;

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestHeader(value = "XRefreshToken ") String refreshToken,
                                                 HttpServletResponse response) {
        jwtService.isTokenValid(refreshToken);

        TokenInfo tokenInfo = authService.reissue(refreshToken);

        Cookie accessTokenCookie = jwtService.bake("Authorization", tokenInfo.getAccessToken(), accessTokenValidity);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = jwtService.bake("XRefreshToken", tokenInfo.getRefreshToken(), refreshTokenValidity);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       HttpServletResponse response) {

        String userId = principalDetails.getUsername();

        //리프레쉬 토큰 삭제
        try {
            jwtRedisService.deleteRefreshToken(userId);
        }catch(TokenCachingException e) {
            //do nothing.
        }

        // Access, refresh token 쿠키 삭제
        Cookie accessTokenCookie = jwtService.bake("Authorization", "", 0L);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie =  jwtService.bake("XRefreshToken", "", 0L);
        response.addCookie(refreshTokenCookie);

        // Security Context 클리어
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }

}
