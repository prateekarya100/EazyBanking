package com.tomcat.Cards;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Cards Microservices",
			 	summary = "Restful web services microservices",
				description = "EazyBank cards microservices restapi documentation",
				version = "v1.2",
				license = @License(
						name = "Apache v2.0",
						url = "https://tomcatdevs.blogspot.com/"
				),
				contact = @Contact(
						name = "Prateek Arya",
						url = "https://tomcatdevs.blogspot.com/",
						email = "prateekarya100@gmail.com"
				)
		),externalDocs = @ExternalDocumentation(
				url = "https://tomcatdevs.blogspot.com/",
		description = "EazyBank Cards Microservices"
)
)
public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
