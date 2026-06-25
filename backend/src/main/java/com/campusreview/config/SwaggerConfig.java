package com.campusreview.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/SpringDoc API 文档配置
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI campusReviewOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("校园课程复习资料共享平台 API")
                        .description("Campus Review — 为全校师生提供课程复习资料共享服务")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CampusReview Team")
                                .email("admin@campus-review.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .name("JWT")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("在下方输入 JWT Token（不需要 Bearer 前缀）")));
    }
}
