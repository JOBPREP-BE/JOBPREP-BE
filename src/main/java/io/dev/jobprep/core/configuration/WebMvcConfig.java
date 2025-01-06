package io.dev.jobprep.core.configuration;

import io.dev.jobprep.common.logging.LoggingInterceptor;
import io.dev.jobprep.system.internal.interceptor.IpWhiteListInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final IpWhiteListInterceptor ipWhiteListInterceptor;
    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipWhiteListInterceptor)
            .addPathPatterns("/internal/**")
            .excludePathPatterns("/api/**", "/login/**");
        registry.addInterceptor(loggingInterceptor)
            .addPathPatterns("/api/v1/**");
    }
}
