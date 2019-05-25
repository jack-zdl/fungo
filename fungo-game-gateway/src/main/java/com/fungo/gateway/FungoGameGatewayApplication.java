package com.fungo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;


@EnableZuulProxy    //开启Zuul的API网关服务
@EnableEurekaClient
@SpringBootApplication
@EnableHystrixDashboard
public class FungoGameGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameGatewayApplication.class, args);
	}





//	@Bean
//	public AccessFilter loginFilter() {
//		return new AccessFilter();
//	}

}
