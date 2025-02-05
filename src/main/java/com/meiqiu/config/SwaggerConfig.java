package com.meiqiu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Description swagger配置
 * @Author sgh
 * @Date 2025/1/22
 * @Time 11:04
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("my-demo")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.meiqiu.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("my-demo接口文档")
                .description("my-demo接口文档，仅供个人实践")
                .version("1.0")
                .termsOfServiceUrl("https://www.baidu.com")
                .contact(contact())
                .build();
    }

    private Contact contact() {
        return new Contact("sgh", "https://www.baidu.com", "guanghuiya@126.com");
    }

}
