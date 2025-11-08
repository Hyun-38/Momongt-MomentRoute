package com.Momongt.Momongt_MomentRoute.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Momongt MomentRoute API")
                        .version("1.0.0")
                        .description("Festival AI Recommendation Web API Documentation")
                        .contact(new Contact()
                                .name("Momongt Team")
                                .url("https://github.com/Hyun-38/Momongt-MomentRoute")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("/").description("Docker Container")
                ));
    }
}

