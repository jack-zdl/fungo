package com.fungo.games.proxy;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.BasTagGroupDto;
import com.game.common.dto.game.ReplyDto;
import com.game.common.dto.user.MemberDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏评论业务调用区
 * @Author lyc
 * @create 2019/5/11 10:22
 */
public interface IEvaluateProxyService {

    /**
     * 迁移微服务后 SystemFeignClient调用 用户成长
     * @param memberId
     * @param code
     * @param inectTaskVirtualCoinTaskCodeIdt
     * @param code1
     * @return
     */
    Map<String, Object> exTask(String memberId,  int code,  int inectTaskVirtualCoinTaskCodeIdt, int code1);

    /**
     * 根据用户id获取authorBean
     * @param memberId
     * @return
     */
    AuthorBean getAuthor(String memberId);

    /**
     * 根据条件判断查询总数
     * @param basActionDto
     * @return
     */
    int getBasActionSelectCount( BasActionDto basActionDto);

    /**
     * 根据条件判断获取ReplyDtoList集合
     * @param replyInputPageDto
     * @return
     */
    Page<CmmCmtReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(ReplyInputPageDto replyInputPageDto);

    /**
     * 根据条件判断获取memberDto对象
     * @param md
     * @return
     */
    MemberDto getMemberDtoBySelectOne(MemberDto md);

    /**
     * 根据用户id获取用户身份图标
     * @param memberId
     * @return
     */
    List<HashMap<String, Object>> getStatusImageByMemberId(String memberId);

    /**
     * 根据判断集合id获取BasTagList集合
     * @param collect
     * @return
     */
    List<BasTagDto> getBasTagBySelectListInId(List<String> collect);

    /**
     * 根据group_id获取BasTag集合
     * @param basTagDto
     * @return
     */
    List<BasTagDto> getBasTagBySelectListGroupId(BasTagDto basTagDto);

    /**
     * 根据id获取cmmcomunity单个对象
     * @param ccd
     * @return
     */
    CmmCommunityDto getCmmCommunitySelectOneById(CmmCommunityDto ccd);

    /**
     * Mqfeign调用
     * @param inviteMemberId
     * @param i
     * @param appVersion
     */
    void push(String inviteMemberId, int i, String appVersion);

    /**
     * 根据bastagid获取basTag对象
     * @param basTagDto
     * @return
     */
    BasTagDto getBasTagBySelectById(BasTagDto basTagDto);

    /**
     * 判断BasTagGroup属性值获取BasTagGroup集合
     * @param basTagGroupDto
     * @return
     */
    List<BasTagGroupDto> getBasTagGroupBySelectList(BasTagGroupDto basTagGroupDto);
}
