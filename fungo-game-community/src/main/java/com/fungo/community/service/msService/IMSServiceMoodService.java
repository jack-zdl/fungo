package com.fungo.community.service.msService;


import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 *      社区-心情微服务业务层
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
public interface IMSServiceMoodService {


    /**
     * 查询 社区心情数据列表
     * @return
     */
    public FungoPageResultDto<MooMoodDto> queryCmmMoodList(@RequestBody MooMoodDto mooMoodDto);


    /**
     *  心情评论的 分页查询
     * @return
     */
    public  FungoPageResultDto<MooMessageDto> queryCmmMoodCommentList(@RequestBody MooMessageDto mooMessageDto);



    /**
     * 查询 社区心情总数
     * @return
     */
    public Integer queryCmmMoodCount(@RequestBody MooMoodDto mooMoodDto);


}
