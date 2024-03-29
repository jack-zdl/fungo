package com.fungo.games.feign;


import com.fungo.games.facede.SystemFacedeHystrixService;
import com.game.common.bean.TagBean;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.index.BannerBean;
import com.game.common.dto.mall.MallBannersInput;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberOutBean;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lidf
 * @date: 2018/3/29
 * @description:
 * @version: 2.0
 */
@FeignClient(name = "FUNGO-GAME-SYSTEM",fallbackFactory = SystemFacedeHystrixService.class)
@RequestMapping(value = "/ms/service/system")
public interface SystemFeignClient {

    @ApiOperation(value = "个人资料", notes = "用户身份校验(配合修改密码操作)")
    @RequestMapping(value = "/api/mine/info", method = RequestMethod.GET)
    ResultDto<MemberOutBean> getUserInfo() throws Exception;

    @RequestMapping(value="/api/developer/test", method= RequestMethod.POST)
    ResultDto<String> test();

    /**
     * 功能描述: 根据用户id集合查询用户详情 state为null就不根据状态查询
     */
    @GetMapping(value = "/listMembersByids")
    ResultDto<List<MemberDto>> listMembersByids(@RequestBody List<String> ids,@RequestParam(value = "state",required = false) Integer state);

    /*****************************************分割线***************************************************************/
    /**
     * 迁移微服务后 SystemFeignClient调用 用户成长
     * @param taskDto
     * @return
     */
    @RequestMapping(value="/exTask", method= RequestMethod.POST)
    ResultDto<Map<String, Object>> exTask(@RequestBody TaskDto taskDto);


    /**
     * 根据用户id获取authorBean
     * @param memberId
     * @return
     */
    @RequestMapping(value="/getAuthor", method= RequestMethod.GET)
    ResultDto<AuthorBean> getAuthor(@RequestParam("memberId") String memberId);

    /**
     * 根据条件判断查询总数
     * @param basActionDto
     * @return
     */
    @RequestMapping(value="/countActionNumGameUse")
    @ApiOperation(value="获取动作数量(比如点赞)--游戏服务使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string")
    })
    ResultDto<Integer> getBasActionSelectCount(@RequestBody BasActionDto basActionDto);

    /**
     * 根据条件判断获取memberDto对象
     * @param id
     * @return
     */
    @GetMapping(value = "/getMembersByid")
    ResultDto<MemberDto> getMembersByid(@RequestParam("memberId") String id);

    /**
     * 根据用户id获取用户身份图标
     * @param memberId
     * @return
     */
    @GetMapping("/getStatusImage")
    @ApiOperation(value="根据用户id获取用户身份图标")
    ResultDto<List<HashMap<String, Object>>> getStatusImageByMemberId(@RequestParam("memberId") String memberId);

    /**
     * 根据判断集合id获取BasTagList集合
     * @param collect
     * @return
     */
    @RequestMapping("/listBasTags")
    ResultDto<List<BasTagDto>> getBasTagBySelectListInId(@RequestBody List<String> collect);

    /**
     * 根据group_id获取BasTag集合
     * @param groupId
     * @return
     */
    @GetMapping("/listBasTagByGroup")
    @ApiOperation(value="根据group id集合获取标签集合")
    ResultDto<List<BasTagDto>> getBasTagBySelectListGroupId(@RequestParam("groupId") String groupId);

    /**
     * Mqfeign调用
     * @param inviteMemberId
     * @param i
     * @param appVersion
     */
    @RequestMapping(value="/api/system/push", method= RequestMethod.POST)
    void push(@RequestParam("inviteMemberId") String inviteMemberId,@RequestParam("code") int i,@RequestParam("appVersion") String appVersion);

    /**
     * 根据bastagid获取basTag对象
     * @param id
     * @return
     */
    @GetMapping("/getBasTagById")
    @ApiOperation(value="根据id获取标签")
    ResultDto<BasTagDto> getBasTagBySelectById(@RequestParam("id") String id);

    /**
     * 判断BasTagGroup属性值获取BasTagGroup集合
     * @param basTagGroupDto
     * @return
     */
    @RequestMapping("/listBasTagGroupByCondition")
    @ApiOperation(value="根据指定条件获取标签集合")
    ResultDto<List<BasTagGroupDto>> getBasTagGroupBySelectList(@RequestBody BasTagGroupDto basTagGroupDto);

    /**
     * 批量获取标签获取
     * @param tags
     * @return
     */
//    @GetMapping("/listSortTags")
    @ApiOperation(value="批量获取标签获取")
    @RequestMapping(value="/listSortTags", method= RequestMethod.GET)
    ResultDto<List<TagBean>> listSortTags (@RequestParam("tags") List<String> tags);

    /**
     * 根据用户会员DTO对象分页查询用户会员
     * @param memberDto
     */
    @RequestMapping(value = "/listMemberDtoPag")
    FungoPageResultDto<MemberDto> listMemberDtoPag(@RequestBody MemberDto memberDto);

    @GetMapping(value = "/listGameHisIds")
    ResultDto<List<String>> listGameHisIds(@RequestParam("memberid") String memberid);


    /**
     *  获取某个游戏的游戏礼包数量
     * @return {goodsCount: 游戏礼包数量key}
     */
    @PostMapping("/mall/game/goods")
    public ResultDto<Map<String, Object>> queryGoodsCountWithGame(@RequestBody MallGoodsInput mallGoodsInput);

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
    public FungoPageResultDto<String> gameListMineDownload( @RequestBody CircleFollowVo circleFollowVo);

    /**
     * 存储并发送消息
     *
     * @param transactionMessageDto
     * @return 返回 -1 失败，1 成功
     */
//    @PostMapping(value = "/saveSendMsg", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
//    ResultDto saveAndSendMessage(@RequestBody TransactionMessageDto transactionMessageDto);

    /**
     * 从数据库删除消息
     * @param messageId 消息ID
     * @return 返回 -1 失败，1 成功
     */
//    @GetMapping(value = "/deleteMsg", produces = "application/json;charset=UTF-8")
//    ResultDto deleteMessageByMessageId(@RequestParam("messageId") Long messageId);

    /**
     * 功能描述: 根据合集id查询点赞数据
     * @auther: Carlos
     * @date: 2019/6/11 11:01
     */
    @ApiOperation(value = "v2.5", notes = "")
    @RequestMapping(value = "/mall/game/queryCollection", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<BannerBean> queryCollection(@RequestBody MallBannersInput mallBannersInput);


    @PostMapping(value = "/addAction")
    @ApiOperation(value="新增用户行为记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "行为类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "targetType",value = "业务类型",paramType = "form",dataType = "integer"),
            @ApiImplicitParam(name = "memberId",value = "会员id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "targetid",value = "业务id",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "内容",paramType = "form",dataType = "string")
    })
    public ResultDto<String> addAction(@RequestBody BasActionDto basActionDto);


    /**
     * 查询游戏是否被收藏
     */
    @PostMapping("/getCollectByGameId")
    public   ResultDto<Map<String,Object>> getCollectByGameId(@RequestParam("gameId") String gameId,@RequestParam("memberId") String memberId);

}
