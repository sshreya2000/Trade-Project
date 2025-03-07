package com.tp.tradexcelsior;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(
				title = "TradeExcelsior API",
				version = "1.0",
				description = "API documentation for the TradeExcelsior Admin Panel"
		)
)
@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
public class TradexcelsiorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradexcelsiorApplication.class, args);
	}
}