package io.dev.jobprep.core.configuration;

import io.dev.jobprep.domain.auth.jwt.filter.JwtAuthenticationFilter;
import io.dev.jobprep.domain.auth.application.CustomOAuth2UserService;
import io.dev.jobprep.domain.auth.jwt.handler.JwtAccessDeniedHandler;
import io.dev.jobprep.domain.auth.jwt.handler.OAuth2SuccessHandler;
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
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint entryPoint;

    @Bean
    public SecurityFilterChain authenticationFilterChain(HttpSecurity http) throws Exception {
        configureCommonSecuritySettings(http);
        return http
            .securityMatchers(matchers -> matchers.requestMatchers("/api/**"))
            .authorizeHttpRequests(
                requests -> {
                    requests.requestMatchers(("/api/v1/oauth2/login-urls")).permitAll(); // URL 목록을 위한 엔드포인트
                    requests.requestMatchers("/api/v1/oauth/authorize/**").permitAll(); // OAuth 인증 시작점
                    requests.requestMatchers(("/login/oauth2/code/**")).permitAll(); // OAuth 리다이렉트 URL
                    requests.requestMatchers("/oauth2/**").permitAll(); // ?
                    requests.requestMatchers(("/api-docs/**")).permitAll();
                    requests.requestMatchers("/swagger-ui/**").permitAll();
                    requests.anyRequest().authenticated();
                }
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth -> oauth
                .authorizationEndpoint(
                    auth -> auth.baseUri("/api/v1/oauth2/authorize"))  // OAuth 시작점 변경
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
            )
            .exceptionHandling(handling -> handling
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(entryPoint))
            .build();
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
