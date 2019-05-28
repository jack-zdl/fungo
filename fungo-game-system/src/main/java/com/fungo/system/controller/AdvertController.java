package com.fungo.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.Banner;
import com.fungo.system.proxy.IGameProxyService;
import com.fungo.system.proxy.IndexProxyService;
import com.fungo.system.service.BannerService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.advert.AdvertOutBean;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.repo.cache.facade.FungoCacheAdvert;
import com.game.common.util.CommonUtils;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "", description = "广告")
public class AdvertController {
    @Autowired
    private BannerService bannerService;
//    @Autowired
//    private GameDao gameDao;
//    @Autowired
//    private CmmPostService postService;

    @Autowired
    private FungoCacheAdvert fungoCacheAdvert;

    @Autowired
    private IndexProxyService indexProxyService;

    @Autowired
    private IGameProxyService iGameProxyService;

    @ApiOperation(value = "首页游戏banner接口(v2.3)", notes = "")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/advert", method = RequestMethod.GET)
    public ResultDto<List<AdvertOutBean>> getAdvert() {

        //from redis
        ResultDto<List<AdvertOutBean>> re = new ResultDto<List<AdvertOutBean>>();
        List<AdvertOutBean> list = null;
        list = (List<AdvertOutBean>) fungoCacheAdvert.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_ADVERT_INDEX, "");
        if (null != list && !list.isEmpty()) {
            re.setData(list);
            return re;
        }

        list = new ArrayList<AdvertOutBean>();
        //获取广告位
        List<Banner> blist = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0001").eq("state", 0).orderBy("sort", false));
        for (Banner banner : blist) {
            AdvertOutBean bean = new AdvertOutBean();
            bean.setBizId(banner.getTargetId());
            bean.setBizType(1);
            bean.setContent(CommonUtils.filterWord(banner.getIntro()));
            bean.setImageUrl(banner.getCoverImage());
            bean.setName(banner.getTag());
            bean.setTitle(CommonUtils.filterWord(banner.getTitle()));
            list.add(bean);
        }

        re.setData(list);

        //redis cache
        fungoCacheAdvert.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_ADVERT_INDEX, "", list);

        return re;
    }


    /**
     * /api/advert接口为了适应pc关键字限制的问题
     * 新开了该同业务功能接口
     * @return
     */
    @ApiOperation(value = "首页游戏banner接口(v2.3)", notes = "")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/adt/bnr", method = RequestMethod.GET)
    public ResultDto<List<AdvertOutBean>> getAdvertWithPc() {

        ResultDto<List<AdvertOutBean>> re = new ResultDto<List<AdvertOutBean>>();
        //from redis
        List<AdvertOutBean> list = (List<AdvertOutBean>) fungoCacheAdvert.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_ADVERT_BNR, "");
        if (null != list && !list.isEmpty()) {
            re.setData(list);
            return re;
        }

        //获取广告位
        List<Banner> blist = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0001").eq("state", 0).orderBy("sort", false));
        if (null != blist && !blist.isEmpty()) {
            list = new ArrayList<AdvertOutBean>();
            for (Banner banner : blist) {
                AdvertOutBean bean = new AdvertOutBean();
                bean.setBizId(banner.getTargetId());
                bean.setBizType(1);
                bean.setContent(CommonUtils.filterWord(banner.getIntro()));
                bean.setImageUrl(banner.getCoverImage());
                bean.setName(banner.getTag());
                bean.setTitle(CommonUtils.filterWord(banner.getTitle()));

                list.add(bean);
            }
            re.setData(list);
            //redis cache
            fungoCacheAdvert.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_ADVERT_BNR, "", list);
        }
        return re;
    }


    @ApiOperation(value = "发现页轮播", notes = "")
    @RequestMapping(value = "/api/recommend/discover", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<Map<String, String>>> discover(@Anonymous MemberUserProfile memberUserPrefile) {

        ResultDto<List<Map<String, String>>> re = new ResultDto<List<Map<String, String>>>();
        List<Map<String, String>> listResult = (List<Map<String, String>>) fungoCacheAdvert.getIndexCache("/api/recommend/discover",
                "");
        if (null != listResult && !listResult.isEmpty()) {
            re.setData(listResult);
            return re;
        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        //获取广告位
        List<Banner> blist = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0002").eq("state", 0).orderBy("sort", false));
        for (Banner banner : blist) {
            Map<String, BigDecimal> rateData = indexProxyService.getRateData(banner.getTargetId());  //gameDao.getRateData(banner.getTargetId());
            Map<String, String> map1 = new HashMap<String, String>();
//				 map1.put("rating",rateData.get("avgRating").toString());
            if (1 == banner.getTargetType()) {
                CmmPostDto cmmPostParam = new CmmPostDto();
                cmmPostParam.setId(banner.getTargetId());
                CmmPostDto post = iGameProxyService.selectCmmPostById(cmmPostParam);  //postService.selectById(banner.getTargetId());
                if (post != null) {
                    map1.put("video", post.getVideo());
                }
            }
            map1.put("objectId", banner.getTargetId());
            map1.put("cover_image", banner.getCoverImage());
            map1.put("type", String.valueOf(banner.getTargetType()));
            list.add(map1);
        }
        re.setData(list);
        //redis cache
        fungoCacheAdvert.excIndexCache(true, "/api/recommend/discover", "", list);
        return re;
    }
}
