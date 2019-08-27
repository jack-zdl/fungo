package com.fungo.games;

//import org.mybatis.spring.annotation.MapperScan;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.fungo.games.feign"})
@EnableCaching
@ComponentScan(basePackages = {"com.*"})
@EnableHystrix
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableTransactionManagement
@EnableRedisHttpSession
public class FungoGamesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FungoGamesApplication.class, args);
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
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大  KB,MB-1000MB
        factory.setMaxFileSize("1024000KB");
        /// 设置总上传数据总大小 100MB
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }

   /* *//**
     * 埋点初始化
     *
     * @return
     *//*
    @Bean
    public AnalysysJavaSdk analysysJavaSdk() {
        System.out.println("释放埋点连接..........................");
        return new AnalysysJavaSdk(new SyncCollecter(BuriedPointUtils.ANALYSYS_SERVICE_URL), BuriedPointUtils.APP_KEY);
    }*/

}
