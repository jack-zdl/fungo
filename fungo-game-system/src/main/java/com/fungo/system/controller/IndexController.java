package com.fungo.system.controller;


import com.fungo.system.service.IIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "首页")
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private IIndexService indexService;





    @ApiOperation(value = "首页(v2.4)", notes = "")
    @RequestMapping(value = "/api/recommend/index", method = RequestMethod.POST)
    @ApiImplicitParams({})
    /*
     * iosChannel (int,optional): 1,2,3 (1:appStore上线,2:appTestFlight开发包,3:appInhouse企业包)
     */
    public FungoPageResultDto<CardIndexBean> recommendList(@Anonymous MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPageDto) {
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
//		if(o != null) {
//			iosChannel = (int)o;
//		}
        return indexService.index(inputPageDto, os, iosChannel, app_channel, appVersion);
    }



//-------
}
