package com.fungo.community.msService.mood;


import com.fungo.community.service.msService.IMSServiceMoodService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.MooMoodDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * 查询社区帖子|文章数据
     * @return
     */
    @PostMapping("/ms/service/cmm/mood/lists")
    public FungoPageResultDto<MooMoodDto> queryCmmMoodList(@RequestBody MooMoodDto mooMoodDto) {

        FungoPageResultDto<MooMoodDto> resultDto = new FungoPageResultDto<MooMoodDto>();

        List<MooMoodDto> cmmPostDtoList = imsServiceMoodService.queryCmmMoodList(mooMoodDto);

        resultDto.setData(cmmPostDtoList);

        return resultDto;
    }


    //--------
}
