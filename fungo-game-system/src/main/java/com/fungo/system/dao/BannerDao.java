package com.fungo.system.dao;

//import com.baomidou.mybatisplus.mapper.BaseMapper;
//import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.fungo.system.dto.PCBannerDto;
import com.fungo.system.entity.Banner;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  * 广告 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-22
 */
@Repository
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

	/**
	 * 功能描述: 获取至今为止仍在进行的运营活动
	 * @auther: dl.zhang
	 * @date: 2019/6/12 10:30
	 */
	List<Banner> beforeNewDateBanner(Pagination page);

	/**
	 * 功能描述: 获取至今为止已过期的运营活动
	 * @return: java.util.List<com.fungo.system.entity.Banner>
	 * @auther: dl.zhang
	 * @date: 2019/6/12 10:30
	 */
	List<Banner> afterNewDateBanner(Pagination   page);

	/**
	 * 功能描述:  首页获取六个每周游戏
	 * @param: []
	 * @auther: dl.zhang
	 * @date: 2019/7/16 19:26
	 */
	List<Banner> getBannerByIndex();

	/**
	 * 功能描述:  首页获取六个每周游戏
	 * @param: []
	 * @auther: dl.zhang
	 * @date: 2019/7/16 19:26
	 */
	List<PCBannerDto> getPCBannerByIndex();
}