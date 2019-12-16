package com.fungo.system.controller.portal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.Banner;
import com.fungo.system.facede.IGameProxyService;
import com.fungo.system.facede.IndexProxyService;
import com.fungo.system.service.BannerService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.repo.cache.facade.FungoCacheAdvert;
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

import static com.game.common.consts.FungoCoreApiConstant.FUNGO_CORE_API_ADVERT_RECOMMEND_DISCOVER;

/**
 * <p>
 *  PC广告
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/25 11:43
 */
@SuppressWarnings("all")
@RestController
@Api(value = "", description = "PC广告")
public class PortalSystemAdvertController {
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

    @ApiOperation(value = "PC2.0发现页轮播", notes = "")
    @RequestMapping(value = "/api/portal/system/recommend/discover", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<Map<String, String>>> discover(@Anonymous MemberUserProfile memberUserPrefile) {

        ResultDto<List<Map<String, String>>> re = new ResultDto<List<Map<String, String>>>();
        List<Map<String, String>> listResult = (List<Map<String, String>>) fungoCacheAdvert.getIndexCache(FUNGO_CORE_API_ADVERT_RECOMMEND_DISCOVER,
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
                cmmPostParam.setState(1);
                CmmPostDto post = iGameProxyService.selectCmmPostById(cmmPostParam);  //postService.selectById(banner.getTargetId());
                if (post != null) {
                    map1.put("video", post.getVideo());
                }
            }
            map1.put("objectId", banner.getTargetId());
            map1.put("cover_image", banner.getCoverImage());
            map1.put("type", String.valueOf(banner.getTargetType()));
            map1.put("actionType", String.valueOf(banner.getActionType()));
            map1.put("href", banner.getHref());
            map1.put("tag", banner.getTag());
            list.add(map1);
        }
        re.setData(list);
        //redis cache
        fungoCacheAdvert.excIndexCache(true, "/api/recommend/discover", "", list);
        return re;
    }
}
