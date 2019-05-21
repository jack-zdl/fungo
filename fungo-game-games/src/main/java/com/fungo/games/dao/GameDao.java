package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.entity.Game;
import com.game.common.bean.HotValue;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface GameDao extends BaseMapper<Game> {
    public boolean updateBatchState(@Param("state")Integer state, @Param("idList")String[] idList);
    public boolean updateTags(@Param("gameId")String gameId,@Param("tags")String tags);
    //平均分
    public HashMap<String, BigDecimal> getRateData(String gameId);
    //获取每个游戏评分所占百分比
    public HashMap<String, BigDecimal> getPercentData(String gameId);
    //获取一天内访问数前十的游戏
    public List<HotValue> getHotValue();
    //获取用户最近评论的游戏
    public List<HashMap<String,Object>> getRecentCommentedGames(Page page, String memberId);
    //根据表名(动态)修改
    Boolean updateCountor(Map<String, String> map);
    //被点赞用户的id
    String getMemberIdByTargetId(Map<String, String> map);
//  平均分20190521复制
    HashMap<String, BigDecimal> getRateData1(String gameId);
}
