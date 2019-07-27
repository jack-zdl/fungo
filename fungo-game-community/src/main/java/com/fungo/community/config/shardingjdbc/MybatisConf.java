package com.fungo.community.config.shardingjdbc;

/**
 * mybatis的配置
 * 
 * @author donghuating
 *
 */
//@Configuration
//@AutoConfigureAfter(DataSource.class)
//@MapperScan(basePackages = "com.fungo.system.dao.*",sqlSessionTemplateRef = "sqlSessionTemplate")
public class MybatisConf {

//    @Autowired
//    private XbDataSource xbDataSource;
//
//    private final DataSource dataSource;
//
//    private DataSource shardingDataSource;
//
//    @Autowired
//    public MybatisConf(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    /**
//     * 获取sqlFactory
//     *
//     * @return
//     * @throws Exception
//     */
//    @Bean(name = "sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
//        /**
//         * sharding-jdbc 产生的DataSource
//         */
//
//        DataSource dataSource = xbDataSource.getShardingDataSource();
////        TransactionFactory transactionFactory = (TransactionFactory) new MyBatisMapperScannerConfig();
////        TransactionFactory transactionFactory = new JdbcTransactionFactory();
////
////        Environment environment = new Environment("development", transactionFactory, dataSource);
////        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration(
////                environment);
////        // Dao层包路径
////        configuration.addMappers(" com.ceying.biz.dao.*");
////        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
////        return sqlSessionFactory;
//
//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
////        Class<?>[] list = new Class[]{AccountsPayableVo.class};
////        sessionFactoryBean.setTypeAliases(new Class[]{AccountsPayableVo.class});
//        sessionFactoryBean.setVfs( SpringBootVFS.class);
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
//    }

//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    @Bean
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(xbDataSource.getShardingDataSource());
//    }

}
