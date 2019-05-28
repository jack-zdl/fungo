package com.fungo.community.service.msService;


import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommentDto;

import java.util.List;

/**
 * <p>
 *      社区-评论微服务业务层
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
public interface IMSServiceCommentService {


    /**
     * 分页查询 二级评论数据列表
     * @return
     */
    public FungoPageResultDto<CmmCmtReplyDto> querySecondLevelCmtList(CmmCmtReplyDto replyDto);
//

    /**
     * 根据创建时间排序 上游游戏业务需求
     * @return
     */
    public FungoPageResultDto<CmmCmtReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(CmmCmtReplyDto replyDto);

    /**
     * 分页查询 一级评论数据列表
     * @return
     */
    public List<CmmCommentDto> queryFirstLevelCmtList(CmmCommentDto cmmCommentDto);



    /**
     * 二级评论总数
     * @return
     */
    public Integer querySecondLevelCmtCount(CmmCmtReplyDto replyDto);
}
