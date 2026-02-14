package com.example.matdongsan.common.config;

import com.example.matdongsan.common.util.auth.CurrentUser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(CurrentUser.class);
    }

    @Value("${swagger.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Matdongsan Application API Document")
                .version("v1")
                .description("Matdongsan Application API 명세서입니다.");

        Server server = new Server()
                .url(serverUrl);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
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
