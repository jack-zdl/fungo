package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.HomePage;
import com.game.common.dto.HomePageDto;

import java.util.List;

/**
 * <p>
  * 游戏标签 Mapper 接口
 * </p>
 *
 * @author Carlos
 * @since 2018-05-09
 */
public interface HomePageDao extends BaseMapper<HomePage> {


    List<HomePage> queryList(HomePageDto homePageDto);

    List<HomePage> queryListByPage(HomePageDto homePageDto);

}