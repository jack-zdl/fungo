package com.fungo.gateway;

import com.fungo.gateway.filter.CustomerGatewayFilter;
import com.fungo.gateway.filter.FungoGatewayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
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


	@Autowired
	private CustomerGatewayFilter customerGatewayFilter;

	@RequestMapping("/")
	public String home() {
		return "Hello world";
	}


	@Bean
	public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/api/portal/games/content/game/number/**")
						.filters(f -> f.filter(customerGatewayFilter)
								.addResponseHeader("X-Response-test", "test"))
						.uri("lb://FUNGO-GAME-GAMES")
						.order(0)
						.id("ribbon-route")
				)
				.build();
	}

}
