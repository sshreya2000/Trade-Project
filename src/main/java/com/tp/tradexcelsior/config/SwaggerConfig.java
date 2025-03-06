package com.tp.tradexcelsior.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
    return new io.swagger.v3.oas.models.OpenAPI()
        .info(new Info()
            .title("Tradexcelsior API")
            .description("API documentation for Tradexcelsior application")
            .version("1.0"));
  }
}