package io.dev.jobprep.domain.security.oauth.presentation;


import io.dev.jobprep.domain.security.jwt.application.AuthService;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import io.dev.jobprep.domain.security.oauth.application.OAuthRedisService;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.common.swagger.template.OauthSwagger;
import io.dev.jobprep.domain.security.oauth.application.dto.OAuthUserInfo;
import io.dev.jobprep.domain.security.oauth.presentation.dto.TokenResponse;
import io.dev.jobprep.domain.security.util.TokenHeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OauthController implements OauthSwagger{

    private final AuthService authService;
    private final OAuthRedisService oAuthRedisService;

    @PostMapping("/callback")
    public ResponseEntity<TokenResponse> callBack(
            @RequestParam(name = "temp_token") String tempToken,
            HttpServletRequest request,
            HttpServletResponse response) {
        Optional<OAuthUserInfo> userInfo = oAuthRedisService.getTemporaryToken(tempToken);

        TokenInfo tokenInfo = authService.generateTokenPair(userInfo.get().getUserId());
        authService.bakeCookieIntoResponse(tokenInfo, response);

        TokenResponse tokenResponse = authService.generateTokenResponse(tokenInfo, request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@CookieValue(value = TokenHeaderConstants.REFRESH_HEADER) String refreshToken,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        TokenInfo tokenInfo = authService.reissue(refreshToken);
        authService.bakeCookieIntoResponse(tokenInfo, response);

        TokenResponse tokenResponse = authService.generateTokenResponse(tokenInfo, request);
        return ResponseEntity.ok(tokenResponse);
    }



    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        authService.deleteFromCache(principalDetails);
        authService.bakeCookieIntoResponse(new TokenInfo(), response);

        return ResponseEntity.ok().build();
    }

}
