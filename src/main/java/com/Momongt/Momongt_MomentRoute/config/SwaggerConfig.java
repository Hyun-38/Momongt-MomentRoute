package com.Momongt.Momongt_MomentRoute.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Security Scheme 이름
        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Momongt MomentRoute API")
                        .version("v1.0.0")
                        .description("여행 경로 및 축제 AI 추천 서비스 API 문서")
                        .contact(new Contact()
                                .name("Momongt Team")
                                .url("https://github.com/Hyun-38/Momongt-MomentRoute")
                                .email("support@momongt.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("로컬 개발 서버"),
                        new Server()
                                .url("/api")
                                .description("도커 컨테이너")
                ))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("JWT 인증 토큰을 입력하세요 (Bearer 제외)")))
                // 전역 보안 요구사항 설정
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}

