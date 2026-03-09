package com.example.s3files.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("S3 File CRUD API")
                .description("S3 바이너리 + MySQL 메타데이터 기반 파일 관리 API")
                .version("v1"));
    }
}
