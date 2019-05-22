package com.fungo.gateway;

import com.fungo.gateway.filter.AccessFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;


@EnableZuulProxy    //开启Zuul的API网关服务
@EnableEurekaClient
@SpringBootApplication
public class FungoGameGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameGatewayApplication.class, args);
	}


	@Bean
	public AccessFilter loginFilter() {
		return new AccessFilter();
	}

	/**
	 * 文件上传配置
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//文件最大  KB,MB-10MB
		factory.setMaxFileSize("10240KB");
		/// 设置总上传数据总大小 100MB
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}


//	@Bean
//	public AccessFilter loginFilter() {
//		return new AccessFilter();
//	}

}
