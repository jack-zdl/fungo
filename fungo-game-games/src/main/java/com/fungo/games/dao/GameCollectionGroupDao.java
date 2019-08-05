package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.GameCollectionGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 游戏合集组 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
@Repository
public interface GameCollectionGroupDao extends BaseMapper<GameCollectionGroup> {

    GameCollectionGroup selectGameCollectionGroupByGameId(@Param("gameId") String gameId);

}