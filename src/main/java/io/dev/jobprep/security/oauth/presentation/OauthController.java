package io.dev.jobprep.security.oauth.presentation;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dev.jobprep.security.jwt.dto.TokenInfo;
import io.dev.jobprep.security.oauth.PrincipalDetails;
import io.dev.jobprep.security.oauth.application.JwtService;
import io.dev.jobprep.security.oauth.application.PrincipalDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OauthController {
    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;

    @GetMapping("/login-urls")
    public ResponseEntity<Map<String, String>> getOAuthUrls() {
        System.out.println("getOAuthUrls method started");

        try {
            // URL 맵 생성 과정 로깅
            System.out.println("Creating OAuth URLs map");
            Map<String, String> oauthUrls = new HashMap<>();

            // 각 URL 추가 시 로깅
            System.out.println("Adding Google OAuth URL");
            oauthUrls.put("google", "/api/v1/oauth2/authorize/google");

            System.out.println("Adding Kakao OAuth URL");
            oauthUrls.put("kakao", "/api/v1/oauth2/authorize/kakao");

            System.out.println("Adding Naver OAuth URL");
            oauthUrls.put("naver", "/api/v1/oauth2/authorize/naver");

            // 최종 맵 내용 로깅
            System.out.println("Final OAuth URLs map: " + oauthUrls);

            // ResponseEntity 생성 전 로깅
            System.out.println("Creating ResponseEntity");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Custom-Debug-Header", "Response-Created")
                    .body(oauthUrls);

        } catch (Exception e) {
            System.err.println("Error occurred in getOAuthUrls: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate OAuth URLs", e);
        } finally {
            System.out.println("getOAuthUrls method completed");
        }
    }


    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@RequestHeader(value = "x-refresh-token") String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        DecodedJWT decodeJWT = jwtService.verifyToken(refreshToken);
        String userId = jwtService.extractUserId(decodeJWT);
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        String userEmail = principalDetails.getEmail();
        String userRoles = principalDetails
                .getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

            //새로운 토큰 페어 생성
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userId, userEmail, userRoles);
        return ResponseEntity.ok()
                .header("Authorization", tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken())
                .header("X-Refresh-Token", tokenInfo.getRefreshToken())
                .build();
    }



}
