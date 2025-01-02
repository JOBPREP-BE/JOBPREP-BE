package io.dev.jobprep.domain.security.oauth.presentation;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dev.jobprep.domain.security.jwt.application.JwtRedisService;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import io.dev.jobprep.common.swagger.template.OauthSwagger;
import io.dev.jobprep.domain.security.oauth.presentation.dto.TokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OauthController implements OauthSwagger{
    private final JwtService jwtService;
    private final JwtRedisService jwtRedisService;
    private final PrincipalDetailsService principalDetailsService;

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueRefreshToken(@RequestHeader(value = "xRefreshToken") String refreshToken) {
        jwtService.isTokenValid(refreshToken);

        //토큰 검증
        DecodedJWT decodeJWT = jwtService.verifyToken(refreshToken);
        String userId = jwtService.extractUserId(decodeJWT);

        //캐싱돼있는지 확인
        if(!jwtRedisService.validateRefreshToken(userId, refreshToken)){
            throw new JWTVerificationException("refresh token absent from whitelist");
        }


        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);

        //새로운 토큰 페어 생성
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userId, principalDetails.getEmail(), principalDetails.getUserRoles());


        jwtRedisService.saveRefreshToken(userId, tokenInfo);

        TokenResponse tokenResponse = TokenResponse.from(tokenInfo);

        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        String userId = principalDetails.getUsername();

        //리프레쉬 토큰 삭제
        jwtRedisService.deleteRefreshToken(userId);

        // Access Token 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", "");
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);  //TODO/daniel: HTTPS 설정 후 true 설정
        response.addCookie(accessTokenCookie);

        // Refresh Token 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);  //TODO/daniel: HTTPS 설정 후 true 설정
        response.addCookie(refreshTokenCookie);

        // Security Context 클리어
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok().build();
    }

}
