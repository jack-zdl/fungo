package com.fungo.gateway;

import com.fungo.gateway.filter.FungoGatewayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;


<<<<<<< HEAD

=======
@EnableEurekaClient
>>>>>>> remotes/origin/Branch_communityV2
@SpringBootApplication
@EnableEurekaClient
public class FungoGameGatewayApplication {

<<<<<<< HEAD
    public static void main(String[] args) {
        SpringApplication.run(FungoGameGatewayApplication.class, args);
    }

    @Bean
    public FungoGatewayFilter gatewayFilter() {
        return new FungoGatewayFilter();
    }
=======
	public static void main(String[] args) {
		SpringApplication.run(FungoGameGatewayApplication.class, args);
	}


	@Bean
	public FungoGatewayFilter gatewayFilter() {
		return new FungoGatewayFilter();
	}
>>>>>>> remotes/origin/Branch_communityV2


}
