package com.changeplusplus.survivorfitness.backendapi.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScans({ @ComponentScan("com.changeplusplus.survivorfitness.backendapi.controller"),
		@ComponentScan("com.changeplusplus.survivorfitness.backendapi.config"),
		@ComponentScan("com.changeplusplus.survivorfitness.backendapi.service")})
@EnableJpaRepositories("com.changeplusplus.survivorfitness.backendapi.repository")
@EntityScan("com.changeplusplus.survivorfitness.backendapi.entity")
public class BackendApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApiApplication.class, args);
	}
}
