package com.fungo.system.controller;

import com.fungo.system.entity.Member;
import com.fungo.system.service.IMemberIncentRiskService;
import com.fungo.system.service.IMemberIncentTaskedService;
import com.fungo.system.service.MemberService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *      V2.4.3用户成长体系之任务业务
 *                              用户任务controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Api(value = "", description = "任务及任务虚拟币规则")
@RestController
public class MemberIncentTaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberIncentTaskController.class);

    @Autowired
    private IMemberIncentTaskedService iMemberIncentTaskService;

    @Autowired
    private IMemberIncentRiskService iMemberIncentRiskService;
    @Autowired
    private MemberService memberService;

/*

    @RequestMapping(value = "/api/user/incents/rule/coins/del", method = RequestMethod.GET)
    public ResultDto<String> removeHonorRules() {

        LOGGER.info("call /api/user/incents/rule/coins/del");
        iMemberIncentTaskService.removeTaskRule();
        return ResultDto.success();

    }
*/


    @ApiOperation(value = "任务及任务虚拟币规则(v2.4.3)", notes = "")
    @RequestMapping(value = "/api/user/incents/rule/coins", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<List<Map<String, Object>>> getHonorRules(MemberUserProfile memberUserPrefile) {

        int task_type = FunGoGameConsts.TASK_RULE_TASK_TYPE_SCOREANDCOIN;

        ResultDto<List<Map<String, Object>>> resultDto = new ResultDto<List<Map<String, Object>>>();

        List<Map<String, Object>> resutList = iMemberIncentTaskService.getTaskRule(task_type);
        if (null != resutList) {
            resultDto.setData(resutList);
        } else {
            return ResultDto.error("-1", "获取任务规则出现异常");
        }
        return resultDto;
    }


    /**
     * 获取用户任务完成进度数据
     * @param memberUserPrefile
     * @return
     */
    @RequestMapping(value = "/api/user/incents/task/progress", method = RequestMethod.GET)
    public ResultDto<List<Map<String, Object>>> getMemberTaskProgress(MemberUserProfile memberUserPrefile) {

        String mb_id = memberUserPrefile.getLoginId();
        int task_type = FunGoGameConsts.TASK_RULE_TASK_TYPE_SCOREANDCOIN;

        ResultDto<List<Map<String, Object>>> resultDto = new ResultDto<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> resutList = iMemberIncentTaskService.getMemberTaskProgress(mb_id, task_type);
            if (null != resutList) {
                resultDto.setData(resutList);
            } else {
                return ResultDto.error("-1", "获取用户任务进度出现异常");
            }
        }catch (Exception e){
            LOGGER.error("获取用户任务进度出现异常",e);
            return ResultDto.error("-1", "获取用户任务进度出现异常");
        }
        return resultDto;
    }


    @ApiOperation(value = "功能权限匹配", notes = "")
    @RequestMapping(value = "/api/user/incents/risk/rank/{fun_idt}", method = RequestMethod.GET)
    public ResultDto<Boolean> isMatchLevel(MemberUserProfile memberUserPrefile, @PathVariable("fun_idt") Integer fun_idt) {

        Member member = memberService.selectById(memberUserPrefile.getLoginId());
        if (member == null) {
            ResultDto.success(false);
        }

        boolean b = iMemberIncentRiskService.isMatchLevel(member.getLevel() + "", fun_idt);

        return ResultDto.success(b);

    }


    @ApiOperation(value = "查询用户是否有新手任务", notes = "")
    @RequestMapping(value = "/api/user/incents/task/checknovicetask", method = RequestMethod.GET)
    public ResultDto<Map<String, Object>> checkeUnfinshedNoviceTask(MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        String os = "";
        os = (String) request.getHeader("os");
        String userId = memberUserPrefile.getLoginId();

        return iMemberIncentRiskService.checkeUnfinshedNoviceTask(userId, os);
    }


    //--------

}
