package com.game.common.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.entity.GameReleaseLog;

/**
 * <p>
  * 游戏 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-12
 */
public interface GameReleaseLogDao extends BaseMapper<GameReleaseLog> {

	public List<GameReleaseLog> getGameReleaseLogs(Page page,Map map);
}