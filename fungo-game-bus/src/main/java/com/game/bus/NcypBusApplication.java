package com.game.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class  NcypBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(NcypBusApplication.class, args);
	}
}
