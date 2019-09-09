package com.fungo.system;


import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fungo.system.config.CustomObjectMapper;
import com.game.common.framework.MyProperties;
import com.game.common.framework.runtime.SpringUtils;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.checkerframework.checker.units.qual.A;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication()
@EnableFeignClients(basePackages = {"com.fungo.system.feign"})
@EnableCaching
@ComponentScan(basePackages = {"com.*"})
@EnableHystrix
//@EnableTurbine
@EnableHystrixDashboard
@EnableCircuitBreaker   //使用@HystrixCommand
@EnableTransactionManagement
@EnableConfigurationProperties(MyProperties.class)
@EnableRedisHttpSession
@Import(SpringUtils.class)
public class FungoGameSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FungoGameSystemApplication.class, args);
	}

	@Autowired
	private CustomObjectMapper customObjectMapper;

	@Bean
	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		//设置日期格式
		ObjectMapper objectMapper = new ObjectMapper( );
//		objectMapper.setSerializerFactory( new SerializerFactory().)
//		objectMapper.setSerializerFactory( new SerializerFactory().w( String.class,  new JsonSerializer<String>() {
//
//					@Override
//					public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//						jsonGenerator.writeString( s.trim() );
//					}
//				}
//			));
		mappingJackson2HttpMessageConverter.setObjectMapper( objectMapper);
		//设置中文编码格式
		List<MediaType> list = new ArrayList<MediaType>();
		list.add( MediaType.APPLICATION_JSON_UTF8);
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
		return mappingJackson2HttpMessageConverter;
	}

	@Bean
	public ServletRegistrationBean getServlet() {
		HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
		registrationBean.setLoadOnStartup(1);
		registrationBean.addUrlMappings("/hystrix.stream");
		registrationBean.setName("HystrixMetricsStreamServlet");
		return registrationBean;
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

/*	*//**
	 * 埋点初始化
	 * @return
	 *//*
	@Bean
	public AnalysysJavaSdk analysysJavaSdk() {
		System.out.println("释放埋点连接..........................");
		return new AnalysysJavaSdk(new SyncCollecter(BuriedPointUtils.ANALYSYS_SERVICE_URL), BuriedPointUtils.APP_KEY);
	}*/
}
