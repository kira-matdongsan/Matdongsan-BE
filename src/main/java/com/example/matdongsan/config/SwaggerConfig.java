package com.example.matdongsan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Matdongsan Application API Document")
                .version("v1")
                .description("Matdongsan Application API 명세서입니다.");

        return new OpenAPI()
                .info(info);
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("1-User API")
                .pathsToMatch("/api/v1/**")
                .pathsToExclude("/api/v1/external/**")
                .build();
    }

    @Bean
    public GroupedOpenApi docsApi() {
        return GroupedOpenApi.builder()
                .group("2-Docs API")
                .pathsToMatch("/docs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi externalApi() {
        return GroupedOpenApi.builder()
                .group("3-External API")
                .pathsToMatch("/api/v1/external/**")
                .build();
    }

    @Bean
    public GroupedOpenApi sampleApi() {
        return GroupedOpenApi.builder()
                .group("4-Sample API")
                .pathsToMatch("/sample/**")
                .build();
    }
}
