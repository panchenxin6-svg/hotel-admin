package com.pcx.hotel_admin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("酒店管理平台 API")
                        .version("1.0.0")
                        .description("Hotel Admin Platform API Documentation")
                        .contact(new Contact()
                                .name("Hotel Admin Team")
                                .email("admin@hotel.com")));
    }
}
