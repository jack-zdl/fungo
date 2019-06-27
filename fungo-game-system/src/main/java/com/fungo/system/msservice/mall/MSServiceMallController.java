package com.fungo.system.msservice.mall;

import com.fungo.system.mall.service.IFungoMallGoodsService;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mall.MallGoodsInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class MSServiceMallController {


    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceMallController.class);


    @Autowired
    private IFungoMallGoodsService iFungoMallGoodsService;

    /**
     *  获取某个游戏的游戏礼包数量
     * @return {goodsCount: 游戏礼包数量key}
     */
    @PostMapping("/ms/service/system/mall/game/goods")
    public ResultDto<Map<String, Object>> queryGoodsCountWithGame(@RequestBody MallGoodsInput mallGoodsInput) throws Exception {
        return iFungoMallGoodsService.queryGoodsCountWithGame(mallGoodsInput);
    }

    //------------
}
