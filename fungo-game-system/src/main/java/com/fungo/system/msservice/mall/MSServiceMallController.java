package com.fungo.system.msservice.mall;

import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.fungo.system.service.IActionService;
import com.game.common.dto.ActionInput;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.BannerBean;
import com.game.common.dto.mall.MallBannersInput;
import com.game.common.dto.mall.MallGoodsInput;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


@RestController
public class MSServiceMallController {


    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceMallController.class);


    @Autowired
    private IFungoMallGoodsService iFungoMallGoodsService;

    @Resource(name = "actionServiceImpl")
    private IActionService actionService;

    /**
     *  获取某个游戏的游戏礼包数量
     * @return {goodsCount: 游戏礼包数量key}
     */
    @PostMapping("/ms/service/system/mall/game/goods")
    public ResultDto<Map<String, Object>> queryGoodsCountWithGame(@RequestBody MallGoodsInput mallGoodsInput) throws Exception {
        return iFungoMallGoodsService.queryGoodsCountWithGame(mallGoodsInput);
    }

    //------------


    @ApiOperation(value="查询合集点赞总数", notes="")
    @RequestMapping(value="/ms/service/system/mall/game/queryCollection", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string")
    })
    public ResultDto<BannerBean> queryCollectionLike(@RequestBody MallBannersInput mallBannersInput) throws Exception {
        return actionService.queryCollectionLike(mallBannersInput);
    }

}
