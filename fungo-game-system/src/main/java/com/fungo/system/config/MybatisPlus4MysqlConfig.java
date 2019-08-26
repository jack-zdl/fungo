package com.fungo.system.config;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
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
@MapperScan({"com.fungo.system.dao", "com.fungo.system.mall.mapper", "com.fungo.system.ts.mq.dao.mapper"})
public class MybatisPlus4MysqlConfig {

    @Autowired
    public DataSource dataSource;

    @Autowired
    private PaginationInterceptor paginationInterceptor;

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
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor
        });
//        DataSource dataSource = xbDataSource.getShardingDataSource();
//        TransactionFactory transactionFactory = (TransactionFactory) new MyBatisMapperScannerConfig();
//        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(
//                environment);
//        // Dao层包路径
//        configuration.addMappers(" com.ceying.biz.dao.*");
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
//        return sqlSessionFactory;

//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
////        Class<?>[] list = new Class[]{AccountsPayableVo.class};
////        sessionFactoryBean.setTypeAliases(new Class[]{AccountsPayableVo.class});
//        sessionFactoryBean.setVfs(SpringBootVFS.class);
////        sessionFactoryBean.setTypeAliasesPackage("com.ceying.biz.vo;com.ceying.biz.dto;com.ceying.biz.query;com.ceying.biz.entity");
////        sessionFactoryBean.setTypeAliasesPackage("com.ceying.biz.query");
////        sessionFactoryBean.setTypeAliasesPackage("com.ceying.biz.entity");
//        //添加XML目录
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        try {
//            sessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapping/*.xml"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }finally {
//            SqlSessionFactory sqlSessionFactory = sessionFactoryBean.getObject();
//            return sqlSessionFactory;
//        }
        return sqlSessionFactory.getObject();
    }

}
