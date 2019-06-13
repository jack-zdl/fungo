package com.fungo.gateway;

import com.fungo.gateway.filter.FungoGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableEurekaClient
@SpringBootApplication
@RestController
public class FungoGameGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameGatewayApplication.class, args);
	}

	@Bean
	public FungoGatewayFilter gatewayFilter() {
		return new FungoGatewayFilter();
	}

	@RequestMapping("/")
	public String home() {
		return "Hello world";
	}


}
