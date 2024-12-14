package io.dev.jobprep.security.jwt;

import io.dev.jobprep.security.jwt.dto.TokenInfo;
import io.dev.jobprep.security.oauth.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidityTime;
    @Value("${jwt.refresh-token-validity}")
    private Long refreshTokenValidityTime;
    //토큰 생성기
    //리프레쉬토큰, 액세스 토큰 재생성
    public TokenInfo generateTokenInfo(String userId, String userEmail, String userRoles ){

        //JWT 토큰 반환
        String accessToken = Jwts.builder()
                .claim("auth" , userRoles )
                .setSubject(userId)
                .claim("email", userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ accessTokenValidityTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+ refreshTokenValidityTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer") // 토큰 타입 설정
                .accessToken(accessToken) // 액세스 토큰 설정
                .refreshToken(refreshToken) // 리프레시 토큰 설정
                .build();

    }

    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        try {
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    //토큰 만료 확인
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    //토큰에서 추출한 claim에서 만료 날짜 추출
    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }

    //토큰 추출한 claim에서 Username(이메일을 뽑아옴)
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //토큰에서 추출한 claim에서 원하는 claim을 가져옴
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //string 토큰에서 모든 claim 추출하여 Claim객체에 저장
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())//sign in key로 JWT parse
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //signInKey를 hmac Sah 알고리즘을 이용해 암호키로 만든다
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}