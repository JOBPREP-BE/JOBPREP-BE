package io.dev.jobprep.core.configuration;

import io.dev.jobprep.security.filter.JwtFilter;
import io.dev.jobprep.security.oauth.application.JwtService;
import io.dev.jobprep.security.oauth.application.CustomOAuth2UserService;
import io.dev.jobprep.security.oauth.OAuth2SuccessHandler;
import io.dev.jobprep.security.oauth.application.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    @Bean
    public SecurityFilterChain authenticationFilterChain(HttpSecurity http) throws Exception {
        configureCommonSecuritySettings(http);
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/oauth2/login-urls",    // URL 목록을 위한 엔드포인트
                                "/api/v1/oauth2/authorize/**",   // OAuth 인증 시작점
                                "/login/oauth2/code/**",         // OAuth 리다이렉트 URL
                                "/oauth2/**"
                        )
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtFilter(jwtService, principalDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(authorization ->
                                authorization.baseUri("/api/v1/oauth2/authorize")  // OAuth 시작점 변경
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
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
