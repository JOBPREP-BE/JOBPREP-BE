package io.dev.jobprep.domain.security.oauth.presentation;

import io.dev.jobprep.domain.security.jwt.application.AuthIntegrationManager;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.common.swagger.template.OauthSwagger;
import io.dev.jobprep.domain.security.oauth.presentation.dto.res.TokenResponse;
import io.dev.jobprep.common.constants.TokenHeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/oauth2")
@RestController
@RequiredArgsConstructor
public class OauthController implements OauthSwagger{

    private final AuthIntegrationManager authIntegrationManager;

    @PostMapping("/callback")
    public ResponseEntity<TokenResponse> callBack(@RequestParam(name = "otp_token") String otpToken,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {

        TokenResponse tokenResponse = authIntegrationManager.issueToken(request, response, otpToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@CookieValue(value = TokenHeaderConstants.REFRESH_HEADER) String refreshToken,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {

        TokenResponse tokenResponse = authIntegrationManager.reissueToken(request, response, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {

        authIntegrationManager.logout(response, principalDetails);
        return ResponseEntity.ok().build();
    }

}