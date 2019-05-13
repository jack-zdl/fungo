package com.fungo.community.msService.community;


import com.fungo.community.service.msService.IMSServiceCommunityService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCommunityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *      社区-社区微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController
public class MSServiceCommunityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceCommunityController.class);

    @Autowired
    private IMSServiceCommunityService imsServiceCommunityService;

    /**
     * 分页查询 社区 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cty/lists")
    public FungoPageResultDto<CmmCommunityDto> queryCmmPostList(@RequestBody CmmCommunityDto cmmCommunityDto) {

        FungoPageResultDto<CmmCommunityDto> resultDto = new FungoPageResultDto<CmmCommunityDto>();

        List<CmmCommunityDto> cmmPostDtoList = imsServiceCommunityService.queryCmmCommunityList(cmmCommunityDto);

        resultDto.setData(cmmPostDtoList);

        return resultDto;
    }


    //--------
}
