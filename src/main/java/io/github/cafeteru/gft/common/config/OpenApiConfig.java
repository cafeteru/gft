package io.github.cafeteru.gft.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${app.version:Unknown}")
  private static String appVersion;

  private static Info getInfo() {
    Contact email = new Contact().email("cafeteru.dev@gmail.com");
    return new Info()
        .title("GFT Java Test")
        .description("GFT Exam Backend Skills")
        .version(appVersion)
        .contact(email);
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().info(getInfo());
  }
}
