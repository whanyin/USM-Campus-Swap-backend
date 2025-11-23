package com.cmt322.usmsecondhand.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("USM Secondhand Market API")
                        .description("USM二手交易平台接口文档")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("USM Market Team")
                                .url("https://github.com/whanyin")
                                .email("support@usm-market.com")));
    }
}