package com.fungo.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明: 创建用于接收来自RabbitMQ消息的消费者SinkReceiver<br>
 * 系统版本: 2.0 <br>
 * 开发人员: zhangdl <br>
 * 开发时间: 2018/3/5 15:24<br>
 */
//@CacheConfig
public class InitBeanConfiguration {

    private static Logger logger = LoggerFactory.getLogger(InitBeanConfiguration.class);


//    @Bean(name = "cacheManager")https://blog.csdn.net/just4you/article/details/88397946
//    public CacheManager CacheManager(EhCacheManagerFactoryBean bean){
//        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        cacheManagerFactoryBean.setShared(true);
//        return bean.getObject();
//    }
}
