package com.fungo.games.controller;



import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.games.entity.GameSurveyRel;
import com.fungo.games.helper.MQClient;
import com.fungo.games.service.GameSurveyRelService;
import com.fungo.games.service.IGameService;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.GameSurveyRelDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * feignService 调用中心
 * @Author lyc
 * @create 2019/5/7 11:48
 */
@RestController
@Api(value = "", description = "feignService 调用中心")
@RequestMapping("/ms/service")
public class FeignServiceController {

    @Autowired
    private IGameService iGameService;

    @Autowired
    private GameSurveyRelService gameSurveyRelService;


    @ApiOperation(value = "更新计数器", notes = "")
    @RequestMapping(value = "/api/update/counter", method = RequestMethod.POST)
    public Boolean updateCounter(@RequestBody Map<String,String> map) {
//        根据表名(动态)修改
        return iGameService.updateCountor(map);
    }

    @ApiOperation(value = "被点赞用户的id", notes = "")
    @RequestMapping(value = "/api/getMemberIdByTargetId", method = RequestMethod.POST)
    String getMemberIdByTargetId(@RequestBody Map<String, String> map){
        return iGameService.getMemberIdByTargetId(map);
    }


    @ApiOperation(value = "查询游戏测试会员关联表总数", notes = "")
    @RequestMapping(value = "/api/selectCount", method = RequestMethod.POST)
    int selectCount(@RequestBody GameSurveyRelDto gameSurveyRelDto){
        GameSurveyRel gameSurveyRel = new GameSurveyRel();
        BeanUtils.copyProperties(gameSurveyRelDto, gameSurveyRel);
        boolean notBlank = StringUtils.isNotBlank(gameSurveyRel.getId());
        return gameSurveyRelService.selectCount(new EntityWrapper<GameSurveyRel>());
    }


    public static void main(String[] args) {
        GameSurveyRel gameSurveyRel = new GameSurveyRel();
//        boolean notBlank = StringUtils.isNotBlank(gameSurveyRel.getAgree());
    }








}
