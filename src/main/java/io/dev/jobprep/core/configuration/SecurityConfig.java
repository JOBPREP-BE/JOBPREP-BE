package io.dev.jobprep.core.configuration;

import io.dev.jobprep.security.filter.JwtFilter;
import io.dev.jobprep.security.jwt.JwtService;
import io.dev.jobprep.security.oauth.CustomOAuth2UserService;
import io.dev.jobprep.security.oauth.OAuth2SuccessHandler;
import io.dev.jobprep.security.oauth.application.PrincipalDetailsService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

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

//    @Bean
//    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/login", "/login/**")
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                );
//        configureCommonSecuritySettings(http);
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain authenticationFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/login/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .authorizeHttpRequests(request ->
//                       request.anyRequest().permitAll()
//              )
                .addFilterBefore(
                        new JwtFilter(jwtService, principalDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(oa -> oa.userService(oAuth2UserService))
                                .successHandler(oAuth2SuccessHandler)

                )
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                ;
        configureCommonSecuritySettings(http);
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
