package com.fungo.system.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.BannerImages;
import com.fungo.system.entity.MemberCoupon;
import org.apache.ibatis.annotations.Param;
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
public interface MemberCouponDao extends BaseMapper<MemberCoupon> {

    List<MemberCoupon> getMemberCouponByRecommendId(@Param("memberId") String memberId);

}