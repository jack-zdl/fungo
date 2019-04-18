package com.game.common.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.bean.HotValue;
import com.game.common.entity.Game;

/**
 * <p>
  * 游戏 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-12
 */
public interface GameDao extends BaseMapper<Game> {

	public boolean updateBatchState(@Param("state")Integer state,@Param("idList")String[] idList);
	public boolean updateTags(@Param("gameId")String gameId,@Param("tags")String tags);
	//平均分
	public HashMap<String, BigDecimal> getRateData(String gameId);
	//获取每个游戏评分所占百分比
	public HashMap<String, BigDecimal> getPercentData(String gameId);
	//获取一天内访问数前十的游戏
	public List<HotValue> getHotValue();
	//获取用户最近评论的游戏
	public List<HashMap<String,Object>> getRecentCommentedGames(Page page,String memberId);
	
}