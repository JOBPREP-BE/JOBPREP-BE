package io.dev.jobprep.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://3.36.225.188",
                        "http://localhost:3000",
                        "http://127.0.0.1:3000"
                )
                .allowedMethods("*")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}
