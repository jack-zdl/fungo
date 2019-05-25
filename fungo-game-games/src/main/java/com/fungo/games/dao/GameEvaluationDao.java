package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.entity.GameEvaluation;
import com.game.common.bean.MemberPulishFromCommunity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 游戏评价 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface GameEvaluationDao extends BaseMapper<GameEvaluation> {

    /**
     * 查询游戏评论表中发表评论大于X条，前Y名的用户
     * @param ecnt
     * @param limitSize
     * @param wathMbsSet
     * @return
     */
    List<String> getRecommendMembersFromEvaluation(@Param("ecnt") long ecnt, @Param("limitSize") long limitSize, @Param("wathMbsSet") List<String> wathMbsSet);

    /**
     * 根据游戏id查询参与评论的用户
     * @param page
     * @param map
     * @return
     */
    List<MemberPulishFromCommunity> getMemberOrder(Page page, Map<String, Object> map);

    /**
     * 用户游戏评测精品数
     * @return
     */
    List<Map> getUserGameReviewBoutiqueNumber();
}