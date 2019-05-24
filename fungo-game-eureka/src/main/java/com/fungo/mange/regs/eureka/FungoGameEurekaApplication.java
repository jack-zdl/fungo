package com.fungo.mange.regs.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FungoGameEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameEurekaApplication.class, args);
	}
}
