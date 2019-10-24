package com.fungo.system.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.BannerImages;
import com.fungo.system.entity.MemberCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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


    boolean updateByRversion(MemberCoupon memberCoupon);
    /**
     * 功能描述: 查詢出被邀請人已經因爲邀請人滿足条件获取免费的优惠券的集合
     * @return: java.util.List<com.fungo.system.entity.MemberCoupon>
     * @date: 2019/10/16 10:06
     */
    List<MemberCoupon> getMemberCouponByRecommendId(@Param("memberId") String memberId, @Param("ids") List<String> ids , @Param("type") String type);

    /**
     * 功能描述: 查詢出被邀請人已經因爲邀請人滿足条件获取免费的优惠券的集合
     * @return: java.util.List<com.fungo.system.entity.MemberCoupon>
     * @date: 2019/10/16 10:06
     */
    List<MemberCoupon> getMemberCouponByInvitee( @Param("ids") List<String> ids , @Param("type") String type,@Param( "couponId" )String couponId);

    List<Map<String, Integer>> getMemberCouponByTypes(@Param("memberId") String memberId, @Param("types")List<String> types);

}