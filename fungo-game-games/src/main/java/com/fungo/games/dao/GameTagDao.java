package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.GameTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 游戏标签 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
public interface GameTagDao extends BaseMapper<GameTag> {

    List<String> selectGameTag(@Param("gameId")String gameId);
}