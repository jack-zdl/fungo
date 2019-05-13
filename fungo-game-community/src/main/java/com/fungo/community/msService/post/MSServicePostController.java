package com.fungo.community.msService.post;


import com.fungo.community.service.msService.IMSServicePostService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmPostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *      社区-帖子|文章微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController
public class MSServicePostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServicePostController.class);

    @Autowired
    private IMSServicePostService imsServicePostService;

    /**
     * 查询社区帖子|文章数据
     * @return
     */
    @PostMapping("/ms/service/cmm/post/lists")
    public FungoPageResultDto<CmmPostDto> queryCmmPostList(@RequestBody CmmPostDto cmmPostDto) {

        FungoPageResultDto<CmmPostDto> resultDto = new FungoPageResultDto<CmmPostDto>();
        List<CmmPostDto> cmmPostDtoList = imsServicePostService.queryCmmPostList(cmmPostDto);
        resultDto.setData(cmmPostDtoList);
        return resultDto;
    }



    /**
     * 精品帖子数大于2的用户数据
     * @return
     */
    @GetMapping("/ms/service/cmm/post/essences")
    public FungoPageResultDto<Map> queryCmmPostEssenceList() {

        FungoPageResultDto<Map> resultDto = new FungoPageResultDto<Map>();
        List<Map> essencePostList = imsServicePostService.getHonorQualificationOfEssencePost();
        resultDto.setData(essencePostList);
        return resultDto;
    }
    //--------
}
