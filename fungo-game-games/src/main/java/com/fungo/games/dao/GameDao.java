package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameReleaseLog;
import com.game.common.bean.HotValue;
import com.game.common.dto.game.BangGameDto;
import com.game.common.dto.game.TagGameDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
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

    //获取上周下载数+评论数 排名前10位的游戏
    public List<HashMap<String,Object>> getGamesByDownload(Page page, String memberId);
    //根据表名(动态)修改
    Boolean updateCountor(Map<String, String> map);
    //被点赞用户的id
    String getMemberIdByTargetId(Map<String, String> map);
//  平均分20190521复制
    HashMap<String, BigDecimal> getRateData1(String gameId);

    @MapKey("id")
    Map<String,Game> listGame(@Param("ids") List<String> ids);

    Game getGameByEvaluateId(String evaluateId);
    List<Game> getGameList(@Param("ids") List<String> ids);

    Integer countGameByTags(TagGameDto tagGameDto);

    List<Game> listGameByTags(TagGameDto tagGameDto);

    List<Game> listBangBySortType(BangGameDto tagGameDto);

    Integer countBangBySortType(@Param("sortType") Integer sortType);


    /**
     *  更新下载量
     */
    void addDownLoadNum(@Param("gameId") String gameId,@Param("boomDownloadNum") Long boomDownloadNum);

    List<Game>  getGmaePage( Page page,@Param("keyword") String keyword);



}
