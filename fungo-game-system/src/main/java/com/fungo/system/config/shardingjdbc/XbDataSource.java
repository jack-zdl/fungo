package com.fungo.system.config.shardingjdbc;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.fungo.system.config.NacosFungoCircleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * sharding-jdbc 配置数据源和分库分表规则
 * 
 * @author donghuating
 *
 */
@Component
public class XbDataSource {

    @Autowired
    private DataSource primaryDataSource;

//    @Autowired
//    public DataSource dataSource;

//    @Autowired
//    @Qualifier("secondaryDataSource")
//    private DataSource secondaryDataSource;

    @Autowired
    private DataSource shardingDataSource;

    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;

    @PostConstruct
    public void init() throws SQLException {
        Map<String, DataSource> map = new HashMap<String, DataSource>();
        map.put("cloud", primaryDataSource);
//        map.put("biz", secondaryDataSource);
        DataSourceRule dataSourceRule = new DataSourceRule(map,"cloud");
        List<TableRule> tableRuleList = new ArrayList<TableRule>();
        List<String> pList = new ArrayList<String>();
//        pList.addAll(nacosFungoCircleConfig.basLog);
        pList.add(nacosFungoCircleConfig.getBasLog());
        pList.add(nacosFungoCircleConfig.getBasLogone());
        pList.add(nacosFungoCircleConfig.getBasLogtwo());
//        for (int i = 0; i < 1; i++) {
//            pList.add("t_biz_tripartiteagreement");
//        }
        List<String> tList = new ArrayList<>();
        tList.add("");
        // 建立逻辑表是t_order，实际表是t_order_0，t_order_1的TableRule
//        TableRule orderTableRule =  TableRule.builder("tripartiteagreement").actualTables(pList).dataSourceRule(dataSourceRule).build();

        /**
         * 这个就是限定以什么字段来进行分库分表
         */
        TableRule tableRule = new TableRule.TableRuleBuilder("logic_bas_log").actualTables(pList).dataSourceRule(dataSourceRule)
                .tableShardingStrategy(new TableShardingStrategy("created_at", new ProgramShardingAlgorithm())).build();
        tableRuleList.add(tableRule);

//        ShardingRule shardingRule = ShardingRule.builder()
//                .dataSourceRule(dataSourceRule)
//                .tableRules(Arrays.asList(orderTableRule))
//                // 增加绑定表--绑定表代表一组表，这组表的逻辑表与实际表之间的映射关系是相同的。
//                // 比如t_order与t_order_item就是这样一组绑定表关系,它们的分库与分表策略是完全相同的,那么可以使用它们的表规则将它们配置成绑定表，绑定表所有路由计算将会只使用主表的策略；
//                .bindingTableRules(Collections.singletonList(new BindingTableRule(Arrays.asList(orderTableRule))))
//                // 指定数据库sharding策略--根据user_id字段的值取模
//                .databaseShardingStrategy(  new DatabaseShardingStrategy("tacontractid", new SingleKeyModuloDatabaseShardingAlgorithm()))
////                // 指定表sharding策略--根据order_id字段的值取模
////                .tableShardingStrategy(new TableShardingStrategy("tacontractid",new ProgramShardingAlgorithm()))
//                .build();
        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule)
                .databaseShardingStrategy(
                        new DatabaseShardingStrategy("created_at", new SingleKeyModuloDatabaseShardingAlgorithm()))
                .tableRules(tableRuleList).build();
        shardingDataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
    }

    public DataSource getShardingDataSource() {
        return shardingDataSource;
    }
}
