package com.fungo.community.msService.comment;


import com.fungo.community.service.msService.IMSServiceCommentService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      社区-评论微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController
public class MSServiceCommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceCommentController.class);

    @Autowired
    private IMSServiceCommentService imsServiceCommentService;

    /**
     * 分页查询 二级评论 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cmt/s/lists")
    public FungoPageResultDto<CmmCmtReplyDto> querySecondLevelCmtList(@RequestBody CmmCmtReplyDto replyDto) {
        FungoPageResultDto<CmmCmtReplyDto> cmmPostDtoList = imsServiceCommentService.querySecondLevelCmtList(replyDto);
        return cmmPostDtoList;
    }
    

    /**
     * 根据创建时间排序 上游游戏业务需求
     * @param replyDto
     * @return
     */
    @PostMapping("/ms/service/cmm/cmt/s/getReplyDtoBysSelectPageOrderByCreatedAt")
    public FungoPageResultDto<CmmCmtReplyDto> getReplyDtoBysSelectPageOrderByCreatedAt(@RequestBody CmmCmtReplyDto replyDto) {
        FungoPageResultDto<CmmCmtReplyDto> cmmPostDtoList = imsServiceCommentService.getReplyDtoBysSelectPageOrderByCreatedAt(replyDto);

        return cmmPostDtoList;
    }

    /**
     * 分页查询 一级评论 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cmt/f/lists")
    public FungoPageResultDto<CmmCommentDto> queryFirstLevelCmtList(@RequestBody CmmCommentDto cmmCommentDto) {

        FungoPageResultDto<CmmCommentDto> resultDto = imsServiceCommentService.queryFirstLevelCmtList(cmmCommentDto);

        return resultDto;
    }


    /**
     * 二级评论总数
     * @return
     */
    @PostMapping("/ms/service/cmm/cmt/s/count")
    public ResultDto<Integer> querySecondLevelCmtCount(@RequestBody CmmCmtReplyDto replyDto) {
        ResultDto<Integer> resultDto = new ResultDto<Integer>();
        Integer cmtCount = imsServiceCommentService.querySecondLevelCmtCount(replyDto);
        resultDto.setData(cmtCount);
        return resultDto;
    }


    //--------
}
