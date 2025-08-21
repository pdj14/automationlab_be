package com.aubotmationlab.be.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aubotmation Lab Backend API")
                        .description("Backend service for Aubotmation Lab with MongoDB. " +
                                "Provides RESTful APIs for managing 3D objects, boxes, walls, and zones.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Aubotmation Lab Team")
                                .email("support@aubotmationlab.com")
                                .url("https://aubotmationlab.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.aubotmationlab.com")
                                .description("Production Server")
                ));
    }
}
