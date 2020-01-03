package com.fungo.system.controller;

import com.fungo.system.service.SystemService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.dto.system.CircleFollow;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.IncentRuleRankDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.util.StringUtil;
import com.game.common.util.annotation.Anonymous;
import com.game.common.vo.MemberFollowerVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @RequestMapping(value = "/followerids", method = RequestMethod.POST)
    public FungoPageResultDto<String> getFollowerUserId(@RequestBody MemberFollowerVo memberFollowerVo){
        FungoPageResultDto<String> re = null;
        try {
            re =  systemService.getFollowerUserId(memberFollowerVo.getMemberId());
        }catch (Exception e){
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
    @RequestMapping(value = "/memberFollowers")
    public FungoPageResultDto<MemberFollowerDto> getMemberFollowerList( @RequestBody MemberFollowerVo memberFollowerVo){
        FungoPageResultDto<MemberFollowerDto> re = null;
        try {
            re =  systemService.getMemberFollowerList(memberFollowerVo);
        }catch (Exception e){
            LOGGER.error("SystemController.getFollowerUserId",e);
            re = FungoPageResultDto.error("-1", "SystemController.getMemberFollowerList执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 社区使用--获取用户与指定用户之间的关注关系
     */
    @RequestMapping(value = "/getMemberFollower1")
    public ResultDto<MemberFollowerDto> getMemberFollower1( @RequestBody MemberFollowerDto memberFollowerDto){
        ResultDto<MemberFollowerDto> re = null;
        try {
            re =  systemService.getMemberFollower1(memberFollowerDto);
        }catch (Exception e){
            LOGGER.error("SystemController.getMemberFollower1",e);
            re = ResultDto.error("-1", "SystemController.getMemberFollower1执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 根据用户Id获取最近浏览圈子行为 8个
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getRecentBrowseCommunityByUserId")
    public ResultDto<List<String>> getRecentBrowseCommunityByUserId(@RequestParam("userId") String userId){
        ResultDto<List<String>> re = null;
        try {
            re =  systemService.getRecentBrowseCommunityByUserId(userId);
        }catch (Exception e){
            LOGGER.error("SystemController.getRecentBrowseCommunityByUserId",e);
            re = ResultDto.error("-1", "SystemController.getMemberFollower1执行service出现异常");
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
    @RequestMapping(value = "/members")
    public FungoPageResultDto<MemberDto> getMemberDtoList(@RequestBody MemberDto memberDto){
        FungoPageResultDto<MemberDto> re = null;
        try {
            re =  systemService.getMemberDtoList(memberDto);
        }catch (Exception e){
            LOGGER.error("SystemController.getFollowerUserId",e);
            re = FungoPageResultDto.error("-1", "SystemController.getMemberFollowerList执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 游戏服务 - 根据用户会员DTO对象分页查询用户会员
     * @param: [memberUserPrefile, memberDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.user.MemberDto>
     */
    @RequestMapping(value = "/listMemberDtoPag")
    public FungoPageResultDto<MemberDto> listMemberDtoPag(@RequestBody MemberDto memberDto){
        FungoPageResultDto<MemberDto> re = null;
        try {
            re =  systemService.listMemberDtoPag(memberDto);
        }catch (Exception e){
            LOGGER.error("SystemController.listMemberDtoPag",e);
            re = FungoPageResultDto.error("-1", "SystemController.listMemberDtoPag执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 根据用户id集合查询用户详情 state为null就不根据状态查询
     */
    @RequestMapping(value = "/listMembersByids")
    public ResultDto<List<MemberDto>> listMembersByids(@RequestBody List<String> ids,@RequestParam(value = "state",required = false) Integer state){
        ResultDto<List<MemberDto>> re = null;
        try {
            re =  systemService.listMembersByids(ids,state);
        }catch (Exception e){
            LOGGER.error("SystemController.listMembersByids",e);
            re = ResultDto.error("-1", "SystemController.listMembersByids执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: .找出官方推荐玩家
     */
    @RequestMapping(value = "/listRecommendedMebmber")
    public ResultDto<List<MemberDto>> listRecommendedMebmber(@RequestParam("limit") Integer limit,@RequestParam("currentMbId") String currentMbId,@RequestBody List<String> wathMbsSet){
        ResultDto<List<MemberDto>> re = null;
        try {
            re =  systemService.listRecommendedMebmber(limit,currentMbId,wathMbsSet);
        }catch (Exception e){
            LOGGER.error("SystemController.listWatchMebmber",e);
            re = ResultDto.error("-1", "SystemController.listWatchMebmber执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 根据用户id查询用户详情
     */
    @GetMapping(value = "/getMembersByid")
    public ResultDto<MemberDto> getMembersByid(@RequestParam(value = "memberId" ,required = false) String id){
        if(StringUtil.isNull(id)){
            return ResultDto.error("-1","用户id不可为空");
        }
        ResultDto<MemberDto> re = null;
        try {
            re =  systemService.getMembersByid(id);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.getMembersByid",e);
            re = ResultDto.error("-1", "SystemController.getMembersByid执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     *  根据用户id和用户权益(等级、身份、荣誉)类型，获取用户权益数据
     */
    @RequestMapping(value = "/listIncentrankeByids")
    public ResultDto<List<IncentRankedDto>> listIncentrankeByids(@RequestBody List<String> ids,@RequestParam("rankType") Integer rankType){
        ResultDto<List<IncentRankedDto>> re = null;
        try {
            re =  systemService.listIncentrankeByids(ids,rankType);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.listIncentrankeByids",e);
            re = ResultDto.error("-1", "SystemController.listIncentrankeByids执行service出现异常");
        }finally {
            return re;
        }
    }


    @RequestMapping(value = "/incentrankes")
    public FungoPageResultDto<IncentRankedDto> getIncentRankedList(@RequestBody IncentRankedDto incentRankedDto){
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
    public ResultDto<List<String>> listFollowerCommunityId(@RequestParam("memberId") String memberId){
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
    @RequestMapping(value = "/countActionNum")
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

    @GetMapping(value = "/countSerchUserName")
    @ApiOperation(value="获取搜索的用户名数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword",value = "要搜索的用户名",paramType = "form",dataType = "string")
    })
    public ResultDto<Integer> countSerchUserName(@RequestParam("keyword") String keyword){
        ResultDto<Integer> re = null;
        try {
            re =  systemService.countSerchUserName(keyword);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.countSerchUserName",e);
            re = ResultDto.error("-1", "SystemController.countSerchUserName执行service出现异常");
        }finally {
            return re;
        }
    }

    @RequestMapping(value = "/countActionNumGameUse")
    @ApiOperation(value="获取动作数量(比如点赞)--游戏服务使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string")
    })
    public ResultDto<Integer> countActionNumGameUse(@RequestBody BasActionDto basActionDto){
        ResultDto<Integer> re = null;
        try {
            re =  systemService.countActionNumGameUse(basActionDto);
        }catch (Exception e){
            LOGGER.error("SystemController.countActionNumGameUse",e);
            re = ResultDto.error("-1", "SystemController.countActionNumGameUse执行service出现异常");
        }finally {
            return re;
        }
    }

    @RequestMapping(value = "/listActionByCondition")
    @ApiOperation(value="根据条件获取动作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "state",value = "状态",paramType = "form",dataType = "integer")
    })
    public ResultDto<List<BasActionDto>> listActionByCondition(@RequestBody BasActionDto basActionDto){
        ResultDto<List<BasActionDto>> re = null;
        try {
            re =  systemService.listActionByCondition(basActionDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.listActionByCondition",e);
            re = ResultDto.error("-1", "SystemController.listActionByCondition执行service出现异常");
        }finally {
            return re;
        }
    }

    @GetMapping(value = "/listGameHisIds")
    @ApiOperation(value="获取历史浏览游戏id集合")
    public ResultDto<List<String>> listGameHisIds(@RequestParam("memberid") String memberid){
        ResultDto<List<String>> re = null;
        try {
            re =  systemService.listGameHisIds(memberid);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SystemController.listGameHisIds",e);
            re = ResultDto.error("-1", "SystemController.listGameHisIds执行service出现异常");
        }finally {
            return re;
        }
    }



    @RequestMapping(value = "/listtargetId")
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
            LOGGER.error("SystemController.addAction",e);
            re = ResultDto.error("-1", "SystemController.addAction执行service出现异常");
        }finally {
            return re;
        }
    }

    @PostMapping(value = "/exTask")
    @ApiOperation(value="执行任务")
    public ResultDto<Map<String, Object>> exTask(@RequestBody TaskDto taskDto){
        ResultDto<Map<String, Object>> re = null;
        try {
            re =  systemService.exTask(taskDto);
        }catch (Exception e){
            LOGGER.error("SystemController.exTask",e);
            re = ResultDto.error("-1", "SystemController.exTask执行service出现异常");
        }finally {
            return re;
        }
    }


    @GetMapping("/getAuthor")
    @ApiOperation(value="获取会员信息")
    public ResultDto<AuthorBean> getAuthor(String memberId){
        ResultDto<AuthorBean> re = null;
        try {
            re = systemService.getAuthor(memberId);
        }catch (Exception e){
            LOGGER.error("SystemController.getAuthor",e);
            re = ResultDto.error("-1", "SystemController.getAuthor执行service出现异常");
        }finally {
            return re;
        }
    }

    @GetMapping("/getAuthorList")
    @ApiOperation(value="获取会员信息")
    public FungoPageResultDto<AuthorBean> getAuthorList(@RequestParam("memberIds") String memberIds){
        FungoPageResultDto<AuthorBean> re = null;
        try {
            List<String> memberList = Arrays.asList(memberIds.split(","));
            re = systemService.getAuthorList(memberList);
        }catch (Exception e){
            LOGGER.error("SystemController.getAuthor",e);
            re = FungoPageResultDto.error("-1", "SystemController.getAuthorList执行service出现异常");
        }finally {
            return re;
        }
    }

    @GetMapping("/getUserCard")
    @ApiOperation(value="获取会员信息")
    public ResultDto<AuthorBean> getUserCard(@RequestParam("cardId") String cardId, @RequestParam(value = "memberId",required = false) String memberId){
        ResultDto<AuthorBean> re = null;
        try {
            re = systemService.getUserCard(cardId,memberId);
        }catch (Exception e){
            LOGGER.error("SystemController.getUserCard",e);
            re = ResultDto.error("-1", "SystemController.getUserCard执行service出现异常");
        }finally {
            return re;
        }
    }

    @GetMapping("/getIncentRuleRankById")
    @ApiOperation(value="获取用户级别、身份、荣誉规则")
    public ResultDto<IncentRuleRankDto> getIncentRuleRankById(@RequestParam("id") String id){
        ResultDto<IncentRuleRankDto> re = null;
        try {

            re = systemService.getIncentRuleRankById(id);
        }catch (Exception e){
            LOGGER.error("SystemController.getIncentRuleRankById",e);
            re = ResultDto.error("-1", "SystemController.getIncentRuleRankById执行service出现异常");
        }finally {
            return re;
        }
    }

    @PostMapping("/updateActionUpdatedAtByCondition")
    @ApiOperation(value="根据指定的行为条件更新行为操作日期")
    public ResultDto<String> updateActionUpdatedAtByCondition(@RequestBody Map<String,Object> map){
        ResultDto<String> re = null;
        try {
            re = systemService.updateActionUpdatedAtByCondition(map);
        }catch (Exception e){
            LOGGER.error("SystemController.updateActionUpdatedAtByCondition",e);
            re = ResultDto.error("-1", "SystemController.updateActionUpdatedAtByCondition执行service出现异常");
        }finally {
            return re;
        }
    }



    @GetMapping("/getStatusImage")
    @ApiOperation(value="根据用户id获取用户身份图标")
   public ResultDto<List<HashMap<String, Object>>> getStatusImage(@RequestParam("memberId") String memberId){
        ResultDto<List<HashMap<String, Object>>> re = null;
        try {
            re = systemService.getStatusImage(memberId);
        }catch (Exception e){
            LOGGER.error("SystemController.getStatusImage",e);
            re = ResultDto.error("-1", "SystemController.getStatusImage执行service出现异常");
        }finally {
            return re;
        }
   }

   /*@RequestMapping("/listBasTags")
   @ApiOperation(value="根据标签id集合获取标签集合")
  public ResultDto<List<BasTagDto>> listBasTags (@RequestBody List<String> collect){
      ResultDto<List<BasTagDto>> re = null;
      try {
          re =  systemService.listBasTags(collect);
      }catch (Exception e){
          LOGGER.error("SystemController.listBasTags",e);
          re = ResultDto.error("-1", "SystemController.listBasTags执行service出现异常");
      }finally {
          return re;
      }
    }*/

   /* @GetMapping("/listSortTags")
   @ApiOperation(value="批量获取标签获取")
  public ResultDto<List<TagBean>> listSortTags (@RequestParam List<String> tags){
      ResultDto<List<TagBean>> re = null;
      try {
          re =  systemService.listSortTags(tags);
      }catch (Exception e){
          LOGGER.error("SystemController.listSortTags",e);
          re = ResultDto.error("-1", "SystemController.listSortTags执行service出现异常");
      }finally {
          return re;
      }
    }*/



  /*  @GetMapping("/listBasTagByGroup")
   @ApiOperation(value="根据group id集合获取标签集合")
  public ResultDto<List<BasTagDto>> listBasTagByGroup (@RequestParam("groupId") String groupId){
      ResultDto<List<BasTagDto>> re = null;
      try {
          re =  systemService.listBasTagByGroup(groupId);
      }catch (Exception e){
          LOGGER.error("SystemController.listBasTagByGroup",e);
          re = ResultDto.error("-1", "SystemController.listBasTagByGroup执行service出现异常");
      }finally {
          return re;
      }
    }*/

 /*   @GetMapping("/getBasTagById")
   @ApiOperation(value="根据id获取标签")
  public ResultDto<BasTagDto> getBasTagById (@RequestParam("id") String id){
      ResultDto<BasTagDto> re = null;
      try {
          re =  systemService.getBasTagById(id);
      }catch (Exception e){
          LOGGER.error("SystemController.getBasTagById",e);
          re = ResultDto.error("-1", "SystemController.getBasTagById执行service出现异常");
      }finally {
          return re;
      }
    }*/

   /* @RequestMapping("/listBasTagGroupByCondition")
    @ApiOperation(value="根据指定条件获取标签集合")
   public ResultDto<List<BasTagGroupDto>> listBasTagGroupByCondition(@RequestBody BasTagGroupDto basTagGroupDto) {
       ResultDto<List<BasTagGroupDto>> re = null;
       try {
           re =  systemService.listBasTagGroupByCondition(basTagGroupDto);
       }catch (Exception e){
           LOGGER.error("SystemController.listBasTagGroupByCondition",e);
           re = ResultDto.error("-1", "SystemController.listBasTagGroupByCondition执行service出现异常");
       }finally {
           return re;
       }
   }*/

    /**
     * 查询指定用户所关注的其他用户列表
     */
    @GetMapping("/listWatchMebmber")
    @ApiOperation(value="查询指定用户所关注的其他用户列表")
    public ResultDto<List<MemberDto>> listWatchMebmber(@RequestParam("limit") Integer limit, @RequestParam("currentMbId") String currentMbId){
        ResultDto<List<MemberDto>> re = null;
        try {
            re =  systemService.listWatchMebmber(limit,currentMbId);
        }catch (Exception e){
            LOGGER.error("SystemController.listWatchMebmber",e);
            re = ResultDto.error("-1", "SystemController.listWatchMebmber执行service出现异常");
        }finally {
            return re;
        }
    }

    /**
     * 功能描述: 根据圈子id查询是否关注
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/circle/follow", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<CircleFollowVo> circleListFollow(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CircleFollowVo circleFollowVo) {
        ResultDto<CircleFollowVo> re = null;
        try {
            re = systemService.circleListFollow(circleFollowVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取活动列表异常",e);
            re = ResultDto.error("-1","获取活动列表异常，请联系管理员");
        }
        return re;
    }

    /**
     * 功能描述: 根据用户id查询关注的圈子
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/circle/mine/follow", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<String> circleListMineFollow(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CircleFollowVo circleFollowVo) {
        FungoPageResultDto<String> re = null;
        try {
            re = systemService.circleListMineFollow(circleFollowVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据圈子id查询是否关注",e);
            re = FungoPageResultDto.error("-1","根据圈子id查询是否关注，请联系管理员");
        }
        return re;
    }

    /**
     * 功能描述: 根据用户id查询下载的游戏
     * @param: [memberUserPrefile, request, inputPageDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/game/mine/download", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<String> gameListMineDownload( @RequestBody CircleFollowVo circleFollowVo) {
        FungoPageResultDto<String> re = null;
        try {
            re = systemService.gameListMineDownload(circleFollowVo);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("根据圈子id查询是否关注",e);
            re = FungoPageResultDto.error("-1","根据圈子id查询是否关注，请联系管理员");
        }
        return re;
    }

    /**
     * 功能描述: 查询用户是否关注
     * @auther: dl.zhang
     * @date: 2019/9/23 14:11
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/member/follow", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String,Object>> getMemberFollow(@RequestBody MemberFollowerVo  memberFollowVo ) {
        ResultDto<Map<String,Object>> re = null;
        try {
            re = systemService.getMemberFollow(memberFollowVo);
        }catch (Exception e){
            LOGGER.error("根据圈子id查询是否关注",e);
            re = ResultDto.error("-1","根据圈子id查询是否关注，请联系管理员");
        }
        return re;
    }


    /**
     * 更新荣誉 勋章 加精次数
     */
    @PostMapping("/updateRankedMedal")
    public ResultDto<String> updateRankedMedal(@RequestParam("userId") String userId, @RequestParam("rankidt") Integer rankidt){
           if(StringUtil.isNull(userId)||rankidt==null){
               return ResultDto.error("-1","参数错误");
           }
           return  systemService.updateRankedMedal(userId,rankidt);
    }

}
