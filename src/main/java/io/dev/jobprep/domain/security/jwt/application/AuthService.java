package io.dev.jobprep.domain.security.jwt.application;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.dev.jobprep.domain.security.jwt.exception.TokenException;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import io.dev.jobprep.domain.security.oauth.domain.PrincipalDetails;
import io.dev.jobprep.domain.security.jwt.application.dto.TokenInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final JwtRedisService jwtRedisService;
    private final PrincipalDetailsService principalDetailsService;

    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidity;

    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidity;

    public TokenInfo reissue(String refreshToken) {
        jwtService.isTokenValid(refreshToken);
        String userId= verifyRefreshToken(refreshToken);
        TokenInfo tokenInfo = generateTokenPair(userId);
        log.info("Refresh token rotated for user: {}", userId);
        return tokenInfo;
    }

    public void deleteFromCache(PrincipalDetails principalDetails){
        SecurityContextHolder.clearContext();
        String userId = principalDetails.getUsername();
      
        try {
            jwtRedisService.deleteRefreshToken(userId);
        }catch(TokenException e) {
            //do nothing.
        }
    }

    public void bakeCookieIntoResponse(TokenInfo tokenInfo, HttpServletResponse response){
      
        Long RefreshDuration = StringUtils.hasText(tokenInfo.getRefreshToken())?refreshTokenValidity : 0L;
        Cookie refreshTokenCookie = jwtService.bake("XRefreshToken", tokenInfo.getRefreshToken(), RefreshDuration);

        response.addCookie(refreshTokenCookie);
    }

    private String verifyRefreshToken(String refreshToken) {

        DecodedJWT decodeJWT = jwtService.verifyNDecodeToken(refreshToken);
        String userId = jwtService.extractUserId(decodeJWT);

        jwtRedisService.validateRefreshToken(userId, refreshToken);

        return userId;
    }

    private TokenInfo generateTokenPair(String userId){
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(userId);
        TokenInfo tokenInfo = jwtService.generateTokenInfo(userId, principalDetails.getEmail(), principalDetails.getUserRoles());

        jwtRedisService.saveRefreshToken(userId, tokenInfo);

        return tokenInfo;
    }




}
