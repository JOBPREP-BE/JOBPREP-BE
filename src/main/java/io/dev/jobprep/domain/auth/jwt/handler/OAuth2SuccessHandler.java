package io.dev.jobprep.domain.auth.jwt.handler;

import io.dev.jobprep.domain.auth.jwt.dao.JwtToken;
import io.dev.jobprep.domain.auth.jwt.dao.PrincipalDetails;
import io.dev.jobprep.domain.auth.jwt.helper.JwtHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String COMNA = ",";

    private final JwtHelper jwtHelper;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userId = principalDetails.getUsername().toString();
        String userEmail = principalDetails.getEmail();
        String userAuthority = principalDetails
                .getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(COMNA));

        JwtToken jwtToken = jwtHelper.sign(userId, userEmail, userAuthority);

        // 헤더설정
        response.setHeader("Authorization", jwtToken.getGrantType() + " " + jwtToken.getAccessToken());
        response.setHeader("X-Refresh-Token", jwtToken.getRefreshToken());

        // 성공 메시지 JSON 작성
        String jsonResponse = "{\"message\": \"Login successful\"}";
        response.getWriter().write(jsonResponse);
    }
}
