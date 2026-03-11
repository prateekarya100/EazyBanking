package com.tomcat.Loans;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Loans Microservices",
				description = "Loans Microservices OpenAPI documentation",
				contact = @Contact(
						name = "Prateek Arya",
						email = "prateekarya100@gmail.com",
				 		url = "https://tomcatdevs.blogspot.com/"
				),
				license = @License(
						name = "Apache Tomcat 2.1",
						url = "https://tomcatdevs.blogspot.com/"
				),
				version = "Loans v2.1",
				summary = "Microservices Restful Web Services"
		),externalDocs = @ExternalDocumentation(
				url = "https://tomcatdevs.blogspot.com/",
				description = "Loans Microservices"
		)
)
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

}
