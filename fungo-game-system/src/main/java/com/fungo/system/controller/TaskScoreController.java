package com.fungo.system.controller;

import com.fungo.system.helper.TaskUrl;
import com.fungo.system.service.IMemberIncentSignInTaskService;
import com.fungo.system.service.IScoreRuleService;
import com.fungo.system.service.ITaskService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 任务积分
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "任务积分")
public class TaskScoreController {


    @Autowired
    private ITaskService taskService;
    @Autowired
    private IMemberIncentSignInTaskService iMemberIncentSignInTaskService;

    @ApiOperation(value = "获得签到信息", notes = "")
    @RequestMapping(value = "/api/rank/info", method = RequestMethod.GET)
    public ResultDto<Map<String, Object>> getMineTaskInfo(MemberUserProfile memberUserPrefile) {
        String userId = memberUserPrefile.getLoginId();
        try {
            // V2.4.6之前业务层
            //return taskService.checkInfo(userId);
            return iMemberIncentSignInTaskService.getMemberSignInInfo(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "获得任务分类", notes = "")
    @RequestMapping(value = "/api/rank/category", method = RequestMethod.GET)
    public ResultDto<List> getTaskCategory(MemberUserProfile memberUserPrefile) {
        return taskService.getTaskCategory();
    }

    @ApiOperation(value = "获得任务列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "任务分类", paramType = "form", dataType = "string")
    })
    @RequestMapping(value = "/api/rank/tasks", method = RequestMethod.GET)
    public ResultDto<List> getTaskList(MemberUserProfile memberUserPrefile, @RequestParam String category_id) {
        String userId = memberUserPrefile.getLoginId();
        try {
            return taskService.getTaskList(userId, category_id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "任务帮助", notes = "")
    @RequestMapping(value = "/api/rank/help", method = RequestMethod.GET)
    public ResultDto<TaskUrl> getTaskHelp(@Anonymous MemberUserProfile memberUserPrefile) {
        ResultDto<TaskUrl> re = new ResultDto<TaskUrl>();
        re.setData(new TaskUrl());
        return re;
    }

    @ApiOperation(value = "签到", notes = "")
    @RequestMapping(value = "/api/rank/checkin", method = RequestMethod.POST)
    public ResultDto<Map> checkIn(MemberUserProfile memberUserPrefile) {
        String userId = memberUserPrefile.getLoginId();
        try {
            //V2.4.6之前业务层
            //return taskService.checkIn(userId);
            return iMemberIncentSignInTaskService.exSignIn(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }

    @ApiOperation(value = "签到格言(2.4.3)", notes = "")
    @RequestMapping(value = "/api/rank/signinmotto", method = RequestMethod.GET)
    public ResultDto<String> DailyMotto(MemberUserProfile memberUserPrefile) throws Exception {
        String userId = memberUserPrefile.getLoginId();
        return taskService.DailyMotto(userId);
    }

    @ApiOperation(value = "task", notes = "")
    @RequestMapping(value = "/api/rank/joinCilcle", method = RequestMethod.GET)
    public ResultDto<String> joinCilcle(MemberUserProfile memberUserPrefile) throws Exception {
        String userId = memberUserPrefile.getLoginId();
        return taskService.DailyMotto(userId);
    }

    /**
     * 功能描述: 关注官方账号
     * @date: 2019/12/24 14:53
     */
    @ApiOperation(value = "task", notes = "")
    @RequestMapping(value = "/api/rank/followUser", method = RequestMethod.GET)
    public ResultDto<String> followUser(MemberUserProfile memberUserPrefile) throws Exception {
        String userId = memberUserPrefile.getLoginId();
        return taskService.followUser( userId);
    }

    /**
     * 功能描述: 关注官方账号
     * @date: 2019/12/24 14:53
     */
    @ApiOperation(value = "task", notes = "")
    @RequestMapping(value = "/api/rank/openpush", method = RequestMethod.GET)
    public ResultDto<String> openPush(MemberUserProfile memberUserPrefile) throws Exception {
        String userId = memberUserPrefile.getLoginId();
        return taskService.openPush( userId);
    }

}
