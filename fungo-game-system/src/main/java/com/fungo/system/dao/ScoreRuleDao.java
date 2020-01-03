package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.ScoreRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 规则 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-07
 */
@Repository
public interface ScoreRuleDao extends BaseMapper<ScoreRule> {

    ScoreRule getScoreRule(@Param("ext2Status")String ext2Status);

    ScoreRule getNewbieScoreRuleById(@Param("id")String id);

    List<ScoreRule> getScoreRuleList(@Param("ext2StatusList") List<String> ext2StatusList);

}