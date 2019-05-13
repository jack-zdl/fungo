package com.fungo.games.controller;



import com.fungo.games.service.IGameToolsService;
import com.game.common.dto.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 游戏工具controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Api(value = "", description = "游戏工具下载")
@RestController
public class GameToolsController {


    private static final Logger LOGGER = LoggerFactory.getLogger(GameToolsController.class);

    @Autowired
    private IGameToolsService iGameToolsService;

    @ApiOperation(value = "游戏工具下载(2.4.3)", notes = "")
    @RequestMapping(value = "/api/tools/game/list/{plaType}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plaType",value = "app平台标识 1 android ,2 iOS ;可选，默认android",
                    paramType = "path",dataType = "int")
    })
    public ResultDto<List<Map<String, Object>>> getGameTools(@PathVariable("plaType") Integer plaType) {

        LOGGER.info("call /api/tools/game/list/{}",plaType);
        ResultDto<List<Map<String, Object>>> listResultDto = new ResultDto<List<Map<String, Object>>>();
        List<Map<String, Object>> gameToolsList = iGameToolsService.getGameTools(plaType);
        if (null != gameToolsList) {
            listResultDto.setData(gameToolsList);
        } else {
            return ResultDto.error("-1", "暂无可用游戏工具");
        }
        return listResultDto;
    }

}
