package io.dev.jobprep.domain.security.auth.application;

import io.dev.jobprep.domain.security.auth.presentation.dto.req.AuthLoginRequest;
import io.dev.jobprep.domain.security.jwt.application.AuthIntegrationManager;
import io.dev.jobprep.domain.security.oauth.presentation.dto.res.TokenResponse;
import io.dev.jobprep.domain.users.application.UserCommonService;
import io.dev.jobprep.domain.users.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCommonService userCommonService;
    private final AuthIntegrationManager authIntegrationManager;

    public TokenResponse login(AuthLoginRequest request) {
        User developer = verifyDeveloper(request.getId());
        return authIntegrationManager.issueTokenViaNonOAuth(developer);
    }

    private User verifyDeveloper(String email) {
        return userCommonService.getUserWithEmail(email);
    }

}
