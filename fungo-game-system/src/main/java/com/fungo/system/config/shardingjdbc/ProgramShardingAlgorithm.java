package com.fungo.system.config.shardingjdbc;

//import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
//import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
//import com.game.common.util.date.DateTools;
import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * table 分片算法
 * 分表算法
 * @author donghuating
 *
 */
public class ProgramShardingAlgorithm implements PreciseShardingAlgorithm<Timestamp> {
    Integer zero =  new Integer(0);

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Timestamp> preciseShardingValue) {
        List list = Arrays.asList(collection.toArray());
            Timestamp dateString = preciseShardingValue.getValue();
            int minutes = dateString.getMinutes();
            int type = minutes % list.size();
            return (String) list.get(type);
    }

//    /**
//     * sql == 规则
//     */
//    @Override
//    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Timestamp> shardingValue) {
//        List list = Arrays.asList(availableTargetNames.toArray());
////        for (int i = 0 ;i <list.size(); i++){
//            Timestamp dateString = shardingValue.getValue();
//            int minutes = dateString.getMinutes(); //  DateTools.getHour(dateString);
//            int type = minutes % list.size();
////            String tableName  = shardingValue.getLogicTableName();
//            return (String) list.get(type);
////            if (zero.equals(type)) {
////                return (String) list.get(i);
////            }else {
////                return (String) list.get(i);
////            }
////        }
////        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * sql in 规则
//     */
//    @Override
//    public Collection<String> doInSharding(Collection<String> availableTargetNames,
//                                           ShardingValue<Timestamp> shardingValue) {
//        Collection<String> result = new LinkedHashSet<String>(availableTargetNames.size());
////        Collection<Integer> values = shardingValue.getValues();
////        for (Integer value : values) {
////            for (String tableNames : availableTargetNames) {
////                if (tableNames.endsWith(value % 2 + "")) {
////                    result.add(tableNames);
////                }
////            }
////        }
//        return result;
//    }
//
//    /**
//     * sql between 规则
//     */
//    @Override
//    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames,
//                                                ShardingValue<Timestamp> shardingValue) {
//        Collection<String> result = new LinkedHashSet<String>(availableTargetNames.size());
////        Range<Integer> range = shardingValue.getValueRange();
////        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
////            for (String each : availableTargetNames) {
////                if (each.endsWith(i % 2 + "")) {
////                    result.add(each);
////                }
////            }
////        }
//        return result;
//    }


}
