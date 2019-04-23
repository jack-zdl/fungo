package com.fungo.community;

//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.fungo.community"})
@EnableCaching
//@MapperScan("com.fungo.community.dao.*")
@ComponentScan(basePackages = {"com.*"})
@EnableHystrix
@EnableTransactionManagement
public class FungoGameCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameCommunityApplication.class, args);
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
}
