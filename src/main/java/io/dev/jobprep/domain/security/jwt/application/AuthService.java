package io.dev.jobprep.domain.security.jwt.application;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final JwtRedisService jwtRedisService;
    private final PrincipalDetailsService principalDetailsService;

    public TokenInfo reissue(String refreshToken) {
        String userId= this.verifyRefreshToken(refreshToken);
        TokenInfo tokenInfo = this.generateTokenPair(userId);
        log.info("Refresh token rotated for user: {}", userId);
        return tokenInfo;
    }

    public String verifyRefreshToken(String refreshToken) {
        DecodedJWT decodeJWT = jwtService.verifyToken(refreshToken);
        String userId = jwtService.extractUserId(decodeJWT);

        if(!jwtRedisService.validateRefreshToken(userId, refreshToken)){
            throw new JWTVerificationException("refresh token absent from whitelist");
        }

        return userId;
    }

    public TokenInfo generateTokenPair(String userId){
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userId, principalDetails.getEmail(), principalDetails.getUserRoles());
        jwtRedisService.saveRefreshToken(userId, tokenInfo);

        return tokenInfo;
    }

}
