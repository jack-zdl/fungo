package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.entity.GameReleaseLog;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 游戏 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-12
 */
public interface GameReleaseLogDao extends BaseMapper<GameReleaseLog> {

	public List<GameReleaseLog> getGameReleaseLogs(Page page, Map map);
}