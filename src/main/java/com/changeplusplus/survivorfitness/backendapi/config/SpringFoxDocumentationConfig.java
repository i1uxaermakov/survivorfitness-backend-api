package com.changeplusplus.survivorfitness.backendapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SpringFoxDocumentationConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfoForApplication());
    }

    private ApiInfo getApiInfoForApplication() {
        return new ApiInfo(
                "Survivor Fitness Backend API",
                "Survivor Fitness Foundation helps cancer survivors regain their health and wellness through one-on-one personal training and nutritional support. " +
                        "\nThis is an API for the internal tracking participants' progress.",
                "Version 1",
                "Free to use",
                new Contact("Ilya Ermakov", "https://github.com/i1uxaermakov", "email"),
                "API License",
                "",
                Collections.emptyList());
    }
}
