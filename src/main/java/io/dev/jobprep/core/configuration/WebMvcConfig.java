package io.dev.jobprep.core.configuration;

import io.dev.jobprep.common.auth.resolver.JwtTokenAnnotationResolver;
import io.dev.jobprep.common.logging.LoggingInterceptor;
import io.dev.jobprep.system.internal.interceptor.IpWhiteListInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final IpWhiteListInterceptor ipWhiteListInterceptor;
    private final LoggingInterceptor loggingInterceptor;
    private final JwtTokenAnnotationResolver jwtTokenAnnotationResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipWhiteListInterceptor)
            .addPathPatterns("/internal/**")
            .excludePathPatterns("/api/**", "/login/**");
        registry.addInterceptor(loggingInterceptor)
            .addPathPatterns("/api/v1/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtTokenAnnotationResolver);
    }
}
