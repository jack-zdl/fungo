package com.fungo.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fungo.system"})
public class FungoGameSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameSystemApplication.class, args);
	}
}
