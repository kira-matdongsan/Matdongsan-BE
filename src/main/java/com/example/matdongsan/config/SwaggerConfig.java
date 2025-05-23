package com.example.matdongsan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Demo Application API Document")
                .version("v0.0.1")
                .description("Demo Application API 명세서입니다.");

        return new OpenAPI()
                .info(info);
    }
}
