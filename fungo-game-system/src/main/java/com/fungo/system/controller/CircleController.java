package com.fungo.system.controller;

import com.fungo.system.entity.Banner;
import com.fungo.system.service.IIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.dto.index.CircleCardDataBean;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.android.Android10Instantiator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@RestController
@Api(value = "", description = "圈子首页")
public class CircleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircleController.class);

    @Autowired
    private IIndexService indexService;

    /**
     * 功能描述: app端获取管控台设置的活动列表及详情
     *
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/api/system/circle/event/list", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<CardIndexBean> circleEventList(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPageDto) {
        FungoPageResultDto<CardIndexBean> re = null;
        try {
            //iOS渠道
            String iosChannel = "";
            String os = "";

            os = (String) request.getAttribute("os");
            if (request.getHeader("iosChannel") != null) {
                iosChannel = request.getHeader("iosChannel");
            }
            //app渠道编码
            String app_channel = request.getHeader("appChannel");
            String appVersion = request.getHeader("appVersion");
            re = indexService.appCircleEventList(inputPageDto, os, iosChannel, app_channel, appVersion);
        } catch (Exception e) {
            LOGGER.error("获取活动列表异常", e);
            re = FungoPageResultDto.error("-1", "获取活动列表异常，请联系管理员");
        }
        return re;
    }


    /**
     * 功能描述: 首页活动位banner数据接口
     *
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: Carlos
     * @date: 2019/10/11 11:01
     */
    @ApiOperation(value = "v2.6", notes = "")
    @RequestMapping(value = "/api/system/circle/event/homePage", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<CircleCardDataBean> queryHomePage(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        ResultDto<CircleCardDataBean> re = null;
        //iOS渠道
        String iosChannel = "";
        String os = "";
        os = (String) request.getAttribute("os");
        if (request.getHeader("iosChannel") != null) {
            iosChannel = request.getHeader("iosChannel");
        }
        //app渠道编码
        String app_channel = request.getHeader("appChannel");
        String appVersion = request.getHeader("appVersion");
        re = indexService.queryHomePage(os, iosChannel, app_channel, appVersion);
        return re;
    }



    /**
     * 功能描述: app端获取管控台设置的启动页banner
     *
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: Carlos
     * @date: 2019/10/11 11:01
     */
    @ApiOperation(value = "v2.6", notes = "")
    @RequestMapping(value = "/api/system/circle/event/queryStartUp", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<CircleCardDataBean>> queryStartUp(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        ResultDto<List<CircleCardDataBean>> re = null;
        //iOS渠道
        String iosChannel = "";
        String os = "";
        os = (String) request.getAttribute("os");
        if (request.getHeader("iosChannel") != null) {
            iosChannel = request.getHeader("iosChannel");
        }
        //app渠道编码
        String app_channel = request.getHeader("appChannel");
        String appVersion = request.getHeader("appVersion");
        re = indexService.queryStartUp(os, iosChannel, app_channel, appVersion);
        return re;
    }


    /**
     * 功能描述: app端获取管控台设置的开屏页banner
     *
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: Carlos
     * @date: 2019/10/11 11:01
     */
    @ApiOperation(value = "v2.6", notes = "")
    @RequestMapping(value = "/api/system/circle/event/queryOpenScreen", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<CircleCardDataBean> queryOpenScreen(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        ResultDto<CircleCardDataBean> re = null;
        //Android  iOS
        String os = "";
        os = (String) request.getAttribute("os");
        re = indexService.queryOpenScreen(os);
        return re;
    }


}
