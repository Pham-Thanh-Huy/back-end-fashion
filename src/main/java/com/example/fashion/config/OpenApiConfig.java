package com.example.fashion.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition()
@Configuration
public class OpenApiConfig   {

    @Bean
    public OpenAPI  customOpenApi(){
        return new OpenAPI()
                .info(new Info().title("Quản lý api trong spring").description("API về fashion")
                        .contact(new Contact().name("huypham").url("https://github.com/Pham-Thanh-Huy")
                                .email("huypham3062k3@gmail.com"))
                        .license(new License().name("cửa hàng bán quần áo fashion").url("http://localhost:3000"))
                        .version("1.0.0"));

    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api")
                .packagesToScan("com.example.backendfruitable")
                .build();
    }


}
