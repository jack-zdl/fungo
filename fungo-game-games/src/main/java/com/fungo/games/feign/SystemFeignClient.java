package com.fungo.games.feign;


import com.game.common.dto.AuthorBean;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberOutBean;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lidf
 * @date: 2018/3/29
 * @description:
 * @version: 2.0
 */
@FeignClient(name = "FUNGO-GAME-SYSTEM")
@RequestMapping(value = "/ms/service/system")
public interface SystemFeignClient {

    @ApiOperation(value = "个人资料", notes = "用户身份校验(配合修改密码操作)")
    @RequestMapping(value = "/api/mine/info", method = RequestMethod.GET)
    ResultDto<MemberOutBean> getUserInfo() throws Exception;

    @RequestMapping(value="/api/developer/test", method= RequestMethod.POST)
    ResultDto<String> test();


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
    @RequestMapping(value="/api/basAction/getBasActionSelectCount", method= RequestMethod.POST)
    int getBasActionSelectCount(@RequestBody BasActionDto basActionDto);

    /**
     * 根据条件判断获取memberDto对象
     * @param md
     * @return
     */
    @RequestMapping(value="/api/member/getMemberDtoBySelectOne", method= RequestMethod.POST)
    MemberDto getMemberDtoBySelectOne(@RequestBody MemberDto md);

    /**
     * 根据用户id获取用户身份图标
     * @param memberId
     * @return
     */
    @RequestMapping(value="/api/member/getStatusImageByMemberId", method= RequestMethod.GET)
    List<HashMap<String, Object>> getStatusImageByMemberId(@RequestParam("memberId") String memberId);

    /**
     * 根据判断集合id获取BasTagList集合
     * @param collect
     * @return
     */
    @RequestMapping(value="/api/system/getBasTagBySelectListInId", method= RequestMethod.POST)
    List<BasTagDto> getBasTagBySelectListInId(@RequestBody List<String> collect);

    /**
     * 根据group_id获取BasTag集合
     * @param basTagDto
     * @return
     */
    @RequestMapping(value="/api/system/getBasTagBySelectListGroupId", method= RequestMethod.POST)
    List<BasTagDto> getBasTagBySelectListGroupId(@RequestBody BasTagDto basTagDto);

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
     * @param basTagDto
     * @return
     */
    @RequestMapping(value="/api/system/getBasTagBySelectById", method= RequestMethod.POST)
    BasTagDto getBasTagBySelectById(@RequestBody BasTagDto basTagDto);

    /**
     * 判断BasTagGroup属性值获取BasTagGroup集合
     * @param basTagGroupDto
     * @return
     */
    @RequestMapping(value="/api/system/getBasTagGroupBySelectList", method= RequestMethod.POST)
    List<BasTagGroupDto> getBasTagGroupBySelectList(@RequestBody BasTagGroupDto basTagGroupDto);

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
}
