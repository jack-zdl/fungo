package com.fungo.games.config.shardingjdbc;

import com.fungo.games.config.NacosFungoCircleConfig;
import io.shardingsphere.core.api.ShardingDataSourceFactory;
import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
import io.shardingsphere.core.api.config.TableRuleConfiguration;
import io.shardingsphere.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.core.rule.TableRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/***
 * sharding-jdbc 配置数据源和分库分表规则
 * 
 * @author donghuating
 *
 */
@Component
@Configuration
public class XbDataSource {

    @Autowired
    private DataSource primaryDataSource;

    private DataSource shardingDataSource;

    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;


    @PostConstruct
    public void shardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        Map<String, DataSource> map = new HashMap<String, DataSource>();
        //分库
        map.put("cloud", primaryDataSource);
        shardingRuleConfig.setDefaultDataSourceName("cloud");
        //分表策略
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        //绑定表规则列表
        this.shardingDataSource = ShardingDataSourceFactory.createDataSource(map, shardingRuleConfig,new HashMap<String, Object>(), new Properties());
    }

    /**
     * 分表策略
     * @return
     */
    private TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration rule = new TableRuleConfiguration();
        //逻辑表名称
        rule.setLogicTable("logic_bas_log");

        List<TableRule> tableRuleList = new ArrayList<>();
        List<String> pList = new ArrayList<String>();
        pList.add(nacosFungoCircleConfig.getBasLog());
        pList.add(nacosFungoCircleConfig.getBasLogone());
        pList.add(nacosFungoCircleConfig.getBasLogtwo());
        //源名 + 表名
        rule.setActualDataNodes(nacosFungoCircleConfig.getBasLog()+","+nacosFungoCircleConfig.getBasLogone()+","+nacosFungoCircleConfig.getBasLogtwo()); //db0.t_order_0, db0.t_order_1,db0.t_order_2, db0.t_order_3,db0.t_order_4, db0.t_order_5,db0.t_order_6, db0.t_order_7
        // 表分片策略
        StandardShardingStrategyConfiguration strategyConfiguration =
                new StandardShardingStrategyConfiguration("created_at", new ProgramShardingAlgorithm());
        rule.setTableShardingStrategyConfig(strategyConfiguration);
        //自增列名称
//        rule.setKeyGeneratorColumnName("id");
        return rule;
    }


    public DataSource getShardingDataSource() {
        return shardingDataSource;
    }
}
