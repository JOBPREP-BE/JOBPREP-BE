package io.dev.jobprep.core.configuration;

import io.dev.jobprep.security.oauth.CustomOAuth2UserService;
import io.dev.jobprep.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(request ->
                        request.anyRequest().permitAll()
                )

                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(oa -> oa.userService(oAuth2UserService))
                                .successHandler(oAuth2SuccessHandler)

                );

        return http.build();
    }

    @Bean
    SecurityFilterChain filterChainApi(HttpSecurity httpSecurity) throws Exception {
        configureCommonSecuritySettings(httpSecurity);
        return httpSecurity.build();
    }

    private void configureCommonSecuritySettings(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable);
    }

}
