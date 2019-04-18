package com.ceying.chx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class FungoGameConfigServer {
	public static void main(String[] args) {
		SpringApplication.run(FungoGameConfigServer.class, args);
	}
}
