package com.fungo.system.controller;

import com.fungo.system.entity.Member;
import com.fungo.system.service.SystemService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
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
    @PostMapping(value = "/changeMemberLevel")
    @ApiOperation(value="变更用户等级", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "level",value = "期望变更到的等级",paramType = "form",dataType = "integer")
    })
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
}
