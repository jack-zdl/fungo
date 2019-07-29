package com.fungo.games.config.shardingjdbc;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
//@AutoConfigureAfter(MybatisConf.class)
public class MyBatisMapperScannerConfig {

    public  MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        // mapper类路径
//        mapperScannerConfigurer.setBasePackage("classpath:mapping/*.xml");
        return mapperScannerConfigurer;
    }
}
