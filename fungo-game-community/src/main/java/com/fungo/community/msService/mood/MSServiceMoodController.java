package com.fungo.community.msService.mood;


import com.fungo.community.service.msService.IMSServiceMoodService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      社区-心情微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController
public class MSServiceMoodController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceMoodController.class);

    @Autowired
    private IMSServiceMoodService imsServiceMoodService;

    /**
     * 查询 社区心情数据列表
     * @return
     */
    @PostMapping("/ms/service/cmm/mood/lists")
    public FungoPageResultDto<MooMoodDto> queryCmmMoodList(@RequestBody MooMoodDto mooMoodDto) {

        FungoPageResultDto<MooMoodDto> resultDto = imsServiceMoodService.queryCmmMoodList(mooMoodDto);
        return resultDto;
    }


    /**
     * 心情评论的 分页查询
     * @return
     */
    @PostMapping("/ms/service/cmm/mood/cmt/lists")
    public FungoPageResultDto<MooMessageDto> queryCmmMoodCommentList(@RequestBody MooMessageDto mooMessageDto) {
        FungoPageResultDto<MooMessageDto> resultDto = imsServiceMoodService.queryCmmMoodCommentList(mooMessageDto);
        return resultDto;
    }


    /**
     * 查询 社区心情总数
     * @return
     */
    @PostMapping("/ms/service/cmm/mood/count")
    public ResultDto<Integer> queryCmmMoodCount(@RequestBody MooMoodDto mooMoodDto) {

        ResultDto<Integer> resultDto = new ResultDto<Integer>();

        Integer cmmMoodCount = imsServiceMoodService.queryCmmMoodCount(mooMoodDto);

        resultDto.setData(cmmMoodCount);

        return resultDto;
    }

    //--------
}
