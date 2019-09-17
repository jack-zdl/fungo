package com.fungo.system;

import com.game.common.framework.MyProperties;
import com.game.common.framework.runtime.SpringUtils;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
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
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.servlet.MultipartConfigElement;

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


//	@Bean
//	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
//		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//		ObjectMapper mapper = mappingJackson2HttpMessageConverter.getObjectMapper();
////		// 为mapper注册一个带有SerializerModifier的Factory，此modifier主要做的事情为：当序列化类型为array，list、set时，当值为空时，序列化成[]
//		mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
//		mappingJackson2HttpMessageConverter.setSupportedMediaTypes( ImmutableList.of( MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));
////		设置中文编码格式
//		List<MediaType> list = new ArrayList<MediaType>();
//		list.add( MediaType.APPLICATION_JSON_UTF8);
//		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
//		return mappingJackson2HttpMessageConverter;
//	}

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
