package com.fungo.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fungo.community"})
//@ComponentScan(basePackages = {"com.ceying.biz"})
public class FungoGameCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameCommunityApplication.class, args);
	}
}
