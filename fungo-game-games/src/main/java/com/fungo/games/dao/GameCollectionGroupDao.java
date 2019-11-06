package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.GameCollectionGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 功能描述: 新增游戏合集是否被删除 and gcg.state = 0
     * @date: 2019/11/6 10:29
     */
    List<GameCollectionGroup> selectGameCollectionGroupByGameId(@Param("gameId") String gameId);

}