package io.dev.jobprep.domain.auth.presentation;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.dev.jobprep.domain.auth.application.dto.res.ReissueResponse;
import io.dev.jobprep.domain.auth.application.AuthService;
import io.dev.jobprep.domain.auth.presentation.dto.req.AuthReissueRequest;
import io.dev.jobprep.domain.auth.presentation.dto.res.AuthTokenResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OauthController {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "x-refresh-token";

    private final AuthService authService;

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

    @PostMapping("/reissue")
    public ResponseEntity<AuthTokenResponse> reissue(
        @RequestHeader(AUTHORIZATION) String accessToken,
        @RequestBody @NonNull AuthReissueRequest request) {

        // TODO: refreshToken 재발급 시에,
        //       1. 만료된 accessToken + refreshToken을 같이 검증하도록 할지?
        //       2. refreshToken만 검증할지?
        ReissueResponse response = authService.reissue(accessToken, request.getRefreshToken());

        // TODO: refreshToken을 reissue하는 과정에서 어떻게 전달해줘야 하는지 고민!
        return ResponseEntity.ok(AuthTokenResponse.of(
            response.getAccessToken(), request.getRefreshToken()
        ));
    }



}
