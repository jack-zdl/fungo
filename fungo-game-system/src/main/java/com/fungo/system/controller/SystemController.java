package com.fungo.system.controller;

import com.fungo.system.entity.Member;
import com.fungo.system.service.SystemService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.vo.MemberFollowerVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>提供给外部使用的接口</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/10
 */
@RestController
@RequestMapping("/ms/service/system")
public class SystemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private SystemService systemService;

    /**
     * 功能描述: 根据用户id查询被关注人的id集合
     * @param:
     * @return: java.util.List<java.lang.String>
     * @auther: dl.zhang
     * @date: 2019/5/10 11:34
     */
    @GetMapping(value = "/followerids")
    public FungoPageResultDto<String> getFollowerUserId(MemberUserProfile memberUserPrefile, @RequestBody MemberFollowerVo memberFollowerVo){
        FungoPageResultDto<String> re = null;
        try {
            re =  systemService.getFollowerUserId(memberFollowerVo.getMemberId());
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.getFollowerUserId",e);
            re = FungoPageResultDto.error("-1", "SystemController.getFollowerUserId执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 根据会员关注粉丝表对象来分页查询集合
     * @param: [memberUserPrefile, memberFollowerVo]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberFollowerDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:15
     */
    @GetMapping(value = "/memberFollowers")
    public FungoPageResultDto<MemberFollowerDto> getMemberFollowerList(MemberUserProfile memberUserPrefile, @RequestBody MemberFollowerVo memberFollowerVo){
        FungoPageResultDto<MemberFollowerDto> re = null;
        try {
            re =  systemService.getMemberFollowerList(memberFollowerVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.getFollowerUserId",e);
            re = FungoPageResultDto.error("-1", "SystemController.getMemberFollowerList执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 根据用户会员DTO对象分页查询用户会员
     * @param: [memberUserPrefile, memberDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberDto>
     * @auther: dl.zhang
     * @date: 2019/5/10 17:41
     */
    @GetMapping(value = "/members")
    public FungoPageResultDto<MemberDto> getMemberDtoList(MemberUserProfile memberUserPrefile, @RequestBody MemberDto memberDto){
        FungoPageResultDto<MemberDto> re = null;
        try {
            re =  systemService.getMemberDtoList(memberDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.getFollowerUserId",e);
            re = FungoPageResultDto.error("-1", "SystemController.getMemberFollowerList执行service出现异常");
        }finally {
            return re;
        }
    }


    @GetMapping(value = "/incentrankes")
    public FungoPageResultDto<IncentRankedDto> getIncentRankedList(MemberUserProfile memberUserPrefile, @RequestBody IncentRankedDto incentRankedDto){
        FungoPageResultDto<IncentRankedDto> re = null;
        try {
            re =  systemService.getIncentRankedList(incentRankedDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.getFollowerUserId",e);
            re = FungoPageResultDto.error("-1", "SystemController.getMemberFollowerList执行service出现异常");
        }finally {
            return re;
        }
    }

   @ApiOperation(value="根据指定用户id变更用户到指定等级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "level",value = "期望变更到的等级",paramType = "form",dataType = "integer")
    })
  @PostMapping(value = "/changeMemberLevel")
    public ResultDto<String> changeMemberLevel(@RequestBody MemberDto memberDto){
        ResultDto<String> re = null;
        if(memberDto.getId()==null||memberDto.getLevel()==null){
            re = ResultDto.error("-1", "SystemController.changeMemberLevel参数缺失");
            return re;
        }
        try {
            re =  systemService.changeMemberLevel(memberDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.changeMemberLevel",e);
            re = ResultDto.error("-1", "SystemController.changeMemberLevel执行service出现异常");
        }finally {
            return re;
        }
    }

    @PostMapping(value = "/decMemberExp")
    @ApiOperation(value="根据用户id扣减经验值账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "exp",value = "希望扣减的经验(非扣减后的经验)",paramType = "form",dataType = "integer")
    })
    public ResultDto<String> decMemberExp(@RequestBody MemberDto memberDto){
        ResultDto<String> re = null;
        if(memberDto.getId()==null||memberDto.getExp()==null){
            re = ResultDto.error("-1", "SystemController.decMemberExp参数缺失");
            return re;
        }
        try {
            re =  systemService.decMemberExp(memberDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.decMemberExp",e);
            re = ResultDto.error("-1", "SystemController.decMemberExp执行service出现异常");
        }finally {
            return re;
        }
    }


    @GetMapping(value = "/listFollowerCommunityId")
    @ApiOperation(value="获取关注社区id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId",value = "用户id",paramType = "form",dataType = "string")
    })
    public ResultDto<List<String>> listFollowerCommunityId(@RequestParam String memberId){
        ResultDto<List<String>> re = null;
        if(memberId==null){
            re = ResultDto.error("-1", "SystemController.listFollowerCommunityId参数缺失");
            return re;
        }
        try {
            re =  systemService.listFollowerCommunityId(memberId);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.listFollowerCommunityId",e);
            re = ResultDto.error("-1", "SystemController.listFollowerCommunityId执行service出现异常");
        }finally {
            return re;
        }
    }
    @GetMapping(value = "/countActionNum")
    @ApiOperation(value="获取动作数量(比如点赞)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "state",value = "状态",paramType = "form",dataType = "integer")
    })
    public ResultDto<Integer> countActionNum(@RequestBody BasActionDto basActionDto){
        ResultDto<Integer> re = null;
        try {
            re =  systemService.countActionNum(basActionDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.countActionNum",e);
            re = ResultDto.error("-1", "SystemController.countActionNum执行service出现异常");
        }finally {
            return re;
        }
    }

    @GetMapping(value = "/listtargetId")
    @ApiOperation(value=" 根据用户id，动作类型，目前类型，状态获取目前id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "state",value = "状态",paramType = "form",dataType = "integer")
    })
    public ResultDto<List<String>> listtargetId(@RequestBody BasActionDto basActionDto){
        ResultDto<List<String>> re = null;
        try {
            re =  systemService.listtargetId(basActionDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.listtargetId",e);
            re = ResultDto.error("-1", "SystemController.listtargetId执行service出现异常");
        }finally {
            return re;
        }
    }

    @PostMapping(value = "/addAction")
    @ApiOperation(value="新增用户行为记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "内容",paramType = "form",dataType = "string")
    })
    public ResultDto<String> addAction(@RequestBody BasActionDto basActionDto){
        ResultDto<String> re = null;
        try {
            re =  systemService.addAction(basActionDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.addAction",e);
            re = ResultDto.error("-1", "SystemController.addAction执行service出现异常");
        }finally {
            return re;
        }
    }







}
