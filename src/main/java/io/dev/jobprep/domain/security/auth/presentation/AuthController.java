package io.dev.jobprep.domain.security.auth.presentation;

import io.dev.jobprep.common.swagger.template.AuthSwagger;
import io.dev.jobprep.domain.security.auth.application.AuthService;
import io.dev.jobprep.domain.security.auth.presentation.dto.req.AuthLoginRequest;
import io.dev.jobprep.domain.security.oauth.presentation.dto.res.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthSwagger {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

}
