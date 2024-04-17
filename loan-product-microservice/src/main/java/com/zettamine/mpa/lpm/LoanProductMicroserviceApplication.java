package com.zettamine.mpa.lpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableTransactionManagement
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Loan Product microservice REST API Documentation", 
								description = "MPA Master Data Processsing Loan product microservice REST API Documentation", 
								version = "v1", 
								contact = @Contact(name = "Project Manager : SuniJoseph", 
												   email = "sunil.j@zettamine.in" ,
												   url = "https://www.zettamine.com"), 
												   license = @License(name = "Loan Product 1.0", 
												                      url = "https://www.zettamine.com")), 
								externalDocs = @ExternalDocumentation(description = "Loan Product microservice REST API Documentation", 
								url = "https://www.zettamine.com/swagger-ui.html"))

public class LoanProductMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanProductMicroserviceApplication.class, args);
	}

}
