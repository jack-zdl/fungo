package com.fungo.system.controller.portal;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.Banner;
import com.fungo.system.service.BannerService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.ResultDto;
import com.game.common.dto.advert.AdvertOutBean;
import com.game.common.repo.cache.facade.FungoCacheAdvert;
import com.game.common.util.CommonUtils;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *  PC端门户-首页
 * @author mxf
 * @update 2019/5/5 16:26
 */
@RestController
public class PortalSystemIndexController {


    @Autowired
    private BannerService bannerService;

    @Autowired
    private FungoCacheAdvert fungoCacheAdvert;


    @ApiOperation(value = "首页游戏banner接口(v2.3)", notes = "")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/portal/index/adt/bnr", method = RequestMethod.GET)
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


//---------
}
