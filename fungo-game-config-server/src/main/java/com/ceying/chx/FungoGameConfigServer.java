package com.ceying.chx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class FungoGameConfigServer {
	public static void main(String[] args) {
		SpringApplication.run(FungoGameConfigServer.class, args);
	}
}
