package com.fungo.system.controller.portal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.Banner;
import com.fungo.system.service.BannerService;
import com.fungo.system.service.portal.PortalSystemIIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.advert.AdvertOutBean;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.repo.cache.facade.FungoCacheAdvert;
import com.game.common.util.CommonUtils;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private PortalSystemIIndexService portalSystemIIndexServiceImpl;

    /**
     * 功能描述: 首页轮播
     * @param: []
     * @return: com.game.common.dto.ResultDto<java.util.List<com.game.common.dto.advert.AdvertOutBean>>
     * @auther: dl.zhang
     * @date: 2019/5/24 16:57
     */
    @ApiOperation(value = "首页游戏banner接口(v2.3)", notes = "")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/portal/system/adt/bnr", method = RequestMethod.GET)
    public ResultDto<List<AdvertOutBean>> getAdvertWithPc() {

        ResultDto<List<AdvertOutBean>> re = new ResultDto<List<AdvertOutBean>>();
        //from redis
        List<AdvertOutBean> list = (List<AdvertOutBean>) fungoCacheAdvert.getIndexCache("/api/portal/index/adt/bnr", "");
        if (null != list && !list.isEmpty()) {
            re.setData(list);
            //return re;
        }

        //获取广告位
        List<Banner> blist = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0001").eq("state", 0).orderBy("sort", false));
        if (null != blist && !blist.isEmpty()) {
            list = new ArrayList<>();
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
            fungoCacheAdvert.excIndexCache(true, "/api/portal/index/adt/bnr", "", list);
        }
        return re;
    }


    /**
     * 功能描述: 首页安利墙和精品文章1和2和社区文章
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/5/27 11:12
     */
    @ApiOperation(value = "首页(v2.4)", notes = "")
    @RequestMapping(value = "/api/portal/system/recommend/index", method = RequestMethod.POST)
    @ApiImplicitParams({})
    /*
     * iosChannel (int,optional): 1,2,3 (1:appStore上线,2:appTestFlight开发包,3:appInhouse企业包)
     */
    public FungoPageResultDto<CardIndexBean> recommendList(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPageDto) {
        return portalSystemIIndexServiceImpl.index(inputPageDto);
    }

}
