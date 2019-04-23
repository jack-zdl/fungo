package com.game.common.config;

import com.game.common.framework.MyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@EnableConfigurationProperties(MyProperties.class)
public class MyAutoConfigure {
	private MyProperties myProperties;
	MyAutoConfigure(MyProperties myProperties){
		this.myProperties=myProperties;
	}

}
