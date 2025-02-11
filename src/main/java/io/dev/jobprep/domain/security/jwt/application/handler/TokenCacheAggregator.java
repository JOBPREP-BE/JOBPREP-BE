package io.dev.jobprep.domain.security.jwt.application.handler;

import io.dev.jobprep.domain.security.jwt.application.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCacheAggregator {

    private final JwtTokenCacheHandler jwtCacheHandler;
    private final CsrfTokenCacheHandler csrfCacheHandler;

    public void cache(String userId, JwtToken jwtToken, CsrfToken csrfToken) {
        this.jwtCacheHandler.cache(userId, jwtToken.getRefreshToken());
        this.csrfCacheHandler.cache(userId, csrfToken.getToken());
    }

    public void verifyCache(String userId, String refreshToken, String csrfToken) {
        this.jwtCacheHandler.verifyCache(userId, refreshToken);
        this.csrfCacheHandler.verifyCache(userId, csrfToken);
    }

    public void evictCache(String userId) {
        this.jwtCacheHandler.evictCache(userId);
        this.csrfCacheHandler.evictCache(userId);
    }

}