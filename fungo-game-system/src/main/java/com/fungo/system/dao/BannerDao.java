package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.Banner;

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