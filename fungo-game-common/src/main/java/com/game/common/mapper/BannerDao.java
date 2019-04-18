package com.game.common.mapper;

import com.game.common.entity.Banner;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 广告 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-22
 */
public interface BannerDao extends BaseMapper<Banner> {

//	public boolean updateState(@Param("state") Integer state,@Param("bannerId") String bannerId);
//	public boolean updateSort(@Param("sort") Integer sort,@Param("bannerId") String bannerId);
	/**
	 * 	获取每日格言 
   		relDate 日期
	 * @param relDate
	 * @return
	 */
	public Banner getDailyMotto(String relDate);
}