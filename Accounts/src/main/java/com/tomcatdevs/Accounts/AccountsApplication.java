package com.tomcatdevs.Accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import jakarta.persistence.Version;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservices RestAPI documentation",
				description = "EazyBank accounts microservices rest api documentation"
				,
				contact = @Contact(
						name = "Prateek Arya",
						email = "prateekarya100@gmail.com",
						url = "https://tomcatdevs.blogspot.com/"
			),
				license = @License(
						name = "Apache Tomcat 2.0",
						url = "https://tomcatdevs.blogspot.com/"
				)
		),
		externalDocs = @ExternalDocumentation(
				url = "https://tomcatdevs.blogspot.com/",
				description = "EazyBank accounts microservices documentation"
		)
)

public class AccountsApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}
}
