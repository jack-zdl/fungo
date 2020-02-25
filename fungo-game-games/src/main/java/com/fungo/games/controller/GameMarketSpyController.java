package com.fungo.games.controller;


import com.fungo.games.controller.portal.PortalHomePageController;
import com.fungo.games.entity.GameFungoappRef;
import com.fungo.games.entity.GameMarketSpy;
import com.fungo.games.service.IGameFungoAppRefService;
import com.fungo.games.service.IGameMarketSpyService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.market.GameFungoAppRefInput;
import com.game.common.dto.market.GameMarketSpyInput;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * <p>
 * wap页面下载游戏数据识别表 前端控制器
 * </p>
 *
 * @author mxf
 * @since 2019-04-22
 */
@RestController
@RequestMapping("/api/mk")
@Api(value = "", description = "wap页面下载游戏数据识别表")
public class GameMarketSpyController {

    private static final Logger LOGGER = LoggerFactory.getLogger( PortalHomePageController.class);

    @Autowired
    private IGameMarketSpyService iGameMarketSpyService;

    @Autowired
    private IGameFungoAppRefService iGameFungoAppRefService;

    /**
     * 记录wap页面下载游戏，app端上报游戏安装数据接口
     * @throws Exception
     */
    @ApiOperation(value = "记录wap页面下载游戏，app端上报游戏安装数据接口", notes = "")
    @RequestMapping(value = "/gm/spy", method = RequestMethod.POST)
    public ResultDto<String> addMarketSpyAction(@Anonymous MemberUserProfile memberprofile, @Valid @RequestBody GameMarketSpyInput gameMarketSpyInput) throws Exception {
        try {
            boolean isAdd = iGameMarketSpyService.addMarketSpyAction(gameMarketSpyInput);
            if (isAdd) {
                return ResultDto.success("成功记录记录wap页面下载游戏，app端上报游戏安装数据");
            }
        } catch (Exception ex) {
            LOGGER.error( "记录wap页面下载游戏，app端上报游戏安装数据接口",ex );
        }
        return ResultDto.error("-1", "记录记录wap页面下载游戏，app端上报游戏安装数据失败");
    }

    /**
     * 验证app端是否安装过某游戏接口
     * @throws Exception
     */
    @ApiOperation(value = "验证app端是否安装过某游戏接口", notes = "")
    @RequestMapping(value = "/gm/gmid", method = RequestMethod.POST)
    public ResultDto<HashMap<String, Object>> queryMarketSpyActionWithGameId(@Anonymous MemberUserProfile memberprofile, @RequestBody GameMarketSpyInput gameMarketSpyInput) throws Exception {
        try {
            boolean isHasParam = false;
            //验证数据至少有一项有值
            String imei = gameMarketSpyInput.getImei();
            String imsi = gameMarketSpyInput.getImsi();
            String uuid = gameMarketSpyInput.getUuid();
            int os = gameMarketSpyInput.getOs();
            String mac = gameMarketSpyInput.getMac();
            String android_id = gameMarketSpyInput.getAndroid_id();
            if (StringUtils.isNotBlank(imei)) {
                isHasParam = true;
            }
            if (StringUtils.isNotBlank(imsi)) {
                isHasParam = true;
            }
            if (StringUtils.isNotBlank(uuid)) {
                isHasParam = true;
            }
            if (0 >= os) {
                isHasParam = true;
            }
            if (StringUtils.isNotBlank(mac)) {
                isHasParam = true;
            }
            if (StringUtils.isNotBlank(android_id)) {
                isHasParam = true;
            }
            if (!isHasParam) {
                return ResultDto.error("-1", "查询参数不能为空");
            }
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("game_id", gameMarketSpyInput.getGame_id());
            GameMarketSpy gameMarketSpy = iGameMarketSpyService.queryMarketSpyAction(gameMarketSpyInput);
            if (null != gameMarketSpy) {
                resultMap.put("is_obtain", gameMarketSpy.getIsObtain());
            } else {
                resultMap.put("is_obtain", 1);// 未安装过
            }
            return ResultDto.success(resultMap);
        } catch (Exception ex) {
            LOGGER.error( "验证app端是否安装过某游戏接口异常",ex );
        }
        return ResultDto.error("-1", "验证app端是否安装过某游戏失败");
    }

    /**
     * 获取游戏与Fungo app Apk捆绑包下载地址接口
     * @throws Exception
     */
    @RequestMapping(value = "/gm/fg/durl", method = RequestMethod.POST)
    public ResultDto<HashMap<String, Object>> queryGameFungoappRef(@Anonymous MemberUserProfile memberprofile, @RequestBody GameFungoAppRefInput gameFungoAppRefInput) throws Exception {
        try {
            boolean isHasParam = false;
            //验证数据至少有一项有值
            String game_id = gameFungoAppRefInput.getGame_id();
            String fungo_channel_id = gameFungoAppRefInput.getFungo_channel_id();
            if (StringUtils.isNotBlank(game_id)) {
                isHasParam = true;
            }
            if (!isHasParam) {
                return ResultDto.error("-1", "查询参数不能为空");
            }
            HashMap<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("game_id", game_id);
            resultMap.put("fungo_app_url", "");
            GameFungoappRef gameFungoappRef = iGameFungoAppRefService.queryGameFungoappRef(game_id, fungo_channel_id);
            if (null != gameFungoappRef) {
                resultMap.put("fungo_app_url", gameFungoappRef.getFungoAppUrl());
            }
            return ResultDto.success(resultMap);
        } catch (Exception ex) {
            LOGGER.error( "获取游戏与Fungo app Apk捆绑包下载地址接口异常",ex );
        }
        return ResultDto.error("-1", "获取游戏与Fungo app Apk捆绑包下载地址接口失败");
    }
}
