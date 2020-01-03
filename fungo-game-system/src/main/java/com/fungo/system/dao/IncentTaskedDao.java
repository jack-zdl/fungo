package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.IncentTasked;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 用户权益-任务完成汇总表 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@Repository
public interface IncentTaskedDao extends BaseMapper<IncentTasked> {

    IncentTasked getIncentTaskedByUserAndType(@Param("memberId") String memberId,@Param("taskType") String taskType);

}