package io.dev.jobprep.core.configuration;

import io.dev.jobprep.domain.security.jwt.filter.JwtAuthenticationFilter;
import io.dev.jobprep.domain.security.jwt.application.JwtService;
import io.dev.jobprep.domain.security.oauth.application.CustomOAuth2UserService;
import io.dev.jobprep.domain.security.oauth.application.OAuth2SuccessHandler;
import io.dev.jobprep.domain.security.oauth.application.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtService jwtService;
    private final PrincipalDetailsService principalDetailsService;
    private final AuthenticationEntryPoint entryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain authenticationFilterChain(HttpSecurity http) throws Exception {
        configureCommonSecuritySettings(http);
        http
            //.securityMatchers(matchers -> matchers.requestMatchers("/api/**", "/oauth2/**"))
            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers(
//                            "/api/v1/oauth2/reissue",   // OAuth 인증 시작점
//                            "/login/oauth2/code/**",         // OAuth 리다이렉트 URL
//                            "/oauth2/authorization/**",
//                            "/api-docs/**",
//                            "/swagger-ui/**",
//                            "/actuator/**",
//                            "/internal/**"
//                    )
//                    .permitAll()
//                    .anyRequest().authenticated()
                      .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth -> oauth
                    .userInfoEndpoint(userInfo -> userInfo
                            .userService(oAuth2UserService))
                    .successHandler(oAuth2SuccessHandler)
            )
            .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // JWT를 사용하므로 세션은 불필요
            );
            ;

        return http.build();
    }

    private void configureCommonSecuritySettings(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable);
    }
}
