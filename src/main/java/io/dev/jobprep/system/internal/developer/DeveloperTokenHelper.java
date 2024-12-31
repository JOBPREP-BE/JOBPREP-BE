package io.dev.jobprep.system.internal.developer;

import io.dev.jobprep.system.internal.developer.token.DevToken;
import io.dev.jobprep.system.internal.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static io.dev.jobprep.exception.code.ErrorCode401.INTERNAL_AUTH_MISSING_CREDENTIALS;

@Slf4j
@Component
public class DeveloperTokenHelper {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${developer.token}")
    private String token;

    public void verify(String token) {
        if (token == null || token.isBlank()) {
            log.warn("Invalid Developer-token type at {}", LocalDateTime.now());
            throw new InternalException(INTERNAL_AUTH_MISSING_CREDENTIALS);
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            log.warn("Invalid Developer-token type at {}", LocalDateTime.now());
            throw new InternalException(INTERNAL_AUTH_MISSING_CREDENTIALS);
        }

        DevToken devToken = DevToken.of(token);
        if (!devToken.verify(this.token)) {
            log.warn("Developer-token verification failed at {}", LocalDateTime.now());
            throw new InternalException(INTERNAL_AUTH_MISSING_CREDENTIALS);
        }
    }
}
