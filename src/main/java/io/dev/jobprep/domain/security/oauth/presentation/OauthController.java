package io.dev.jobprep.domain.security.oauth.presentation;


import com.auth0.jwt.interfaces.DecodedJWT;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import io.dev.jobprep.common.swagger.template.OauthSwagger;
import io.dev.jobprep.domain.security.oauth.presentation.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OauthController implements OauthSwagger{
    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;
    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> refreshToken(@RequestHeader(value = "xRefreshToken") String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        DecodedJWT decodeJWT = jwtService.verifyToken(refreshToken);
        String userId = jwtService.extractUserId(decodeJWT);

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);

        //새로운 토큰 페어 생성
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userId, principalDetails.getEmail(), principalDetails.getUserRoles());
        TokenResponse tokenResponse = TokenResponse.from(tokenInfo);
        return ResponseEntity.ok()
                .body(tokenResponse);
    }



}
