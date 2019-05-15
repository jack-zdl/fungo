package com.fungo.system.config;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import javax.sql.DataSource;


@Configuration
@MapperScan({"com.fungo.system.dao","com.fungo.system.mall.mapper","com.fungo.system.ts.mq.dao.mapper"})
public class MybatisPlus4MysqlConfig {

    @Autowired
    public DataSource dataSource;

    @Primary
    @Bean("mysqlSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapping/*.xml"));
//        sqlSessionFactory.setPlugins(new Interceptor[]{
//                new PaginationInterceptor(),
//                new PerformanceInterceptor(),
//                new OptimisticLockerInterceptor()
//        });
        return sqlSessionFactory.getObject();
    }

}
