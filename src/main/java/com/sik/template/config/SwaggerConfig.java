package com.sik.template.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Template Springboot API",
                description = "Template Springboot API Description.",
                version = "v0.0.1"

        ),
        tags = {

        },
        security = {
                @SecurityRequirement(name = "X-AUTH-TOKEN"),
        }
)
@SecuritySchemes({
        @SecurityScheme(
                name = "X-AUTH-TOKEN",
                type = SecuritySchemeType.APIKEY,
                description = "JWT Token",
                in = SecuritySchemeIn.HEADER,
                paramName = "X-AUTH-TOKEN"
        )
})
@Configuration
public class SwaggerConfig {
}
