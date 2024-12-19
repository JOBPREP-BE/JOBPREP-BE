package io.dev.jobprep.domain.auth.application;

import static io.dev.jobprep.exception.code.ErrorCode401.AUTH_MISSING_CREDENTIALS;

import io.dev.jobprep.domain.auth.application.dto.res.ReissueResponse;
import io.dev.jobprep.domain.auth.exception.AuthException;
import io.dev.jobprep.domain.auth.jwt.dao.PrincipalDetails;
import io.dev.jobprep.domain.auth.jwt.helper.JwtHelper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String EXP_PREFIX = "exp";
    private static final String DELIMITER = ",";

    private final JwtHelper jwtHelper;
    private final PrincipalDetailsService principalDetailsService;

    public ReissueResponse reissue(String expiredAccessToken, String refreshToken) {

        log.info("reissue expiredAccessToken: {}", expiredAccessToken);
        log.info("reissue refreshToken: {}", refreshToken);

        expiredAccessToken = expiredAccessToken.substring(7);

        // TODO: 저장되어 있는 refreshToken을 가져와서, 전달받은 refreshToken와 비교하도록 구현
        String verifiedRefreshToken = null;
        if (!verifiedRefreshToken.equals(refreshToken)) {
            log.info("refreshToken not matched!");
            throw new AuthException(AUTH_MISSING_CREDENTIALS);
        }
        String accssToken = generateAccessToken(expiredAccessToken);
        int refreshTokenExpirySeconds = calculateRefreshTOkenExpirySeconds(refreshToken);
        // TODO: 새로 재발급한 토큰들 저장
        return ReissueResponse.from(accssToken);
    }

    private String generateAccessToken(String expiryAccessToken) {
        var decodedJWT = jwtHelper.verifyWithoutExpiry(expiryAccessToken);
        String userId = decodedJWT.getSubject();

        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        String userEmail = principalDetails.getEmail();
        String userRole = principalDetails
            .getAuthorities()
            .stream().map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(DELIMITER));

        return jwtHelper.sign(userId, userEmail, userRole).getAccessToken();
    }

    private int calculateRefreshTOkenExpirySeconds(String refreshToken) {
        var claims = jwtHelper.verifyRefreshToken(refreshToken).getClaims();
        return (int) (claims.get(EXP_PREFIX).asLong() - System.currentTimeMillis()  / 1000);
    }

}
