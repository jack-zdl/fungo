package com.game.common.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
//@MapperScan(basePackages={"com.fungo.*.dao.*","com.fungo.community.dao.mapper.*"})
public class MybatisPlusConfig {  
     /** 
     *   mybatis-plus分页插件 
     */  
    @Bean  
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();  
        page.setDialectType("mysql");  
        return page;  
    }  
}  