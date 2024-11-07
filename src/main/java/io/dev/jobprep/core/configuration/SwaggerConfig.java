package io.dev.jobprep.core.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
        Info info = new Info()
            .title("JOBPREB API")
            .version(springdocVersion)
            .description("JOBPREB API 문서입니다.");

        String jwtScheme = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtScheme);
        Components components = new Components()
            .addSecuritySchemes(jwtScheme, new SecurityScheme()
                .name("Authorization")
                .type(Type.HTTP)
                .in(In.HEADER)
                .scheme("Bearer")
                .bearerFormat("JWT"));

        return new OpenAPI()
            .components(new Components())
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components);

    }

}
