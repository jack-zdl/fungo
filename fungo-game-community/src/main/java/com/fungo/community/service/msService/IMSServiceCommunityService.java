package com.fungo.community.service.msService;


import com.game.common.dto.community.CmmCommunityDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 *      社区 - 社区微服务业务层
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
public interface IMSServiceCommunityService {


    /**
     * 查询社区数据列表
     * @return
     */
    public List<CmmCommunityDto> queryCmmCommunityList(@RequestBody CmmCommunityDto communityDto);

    /**
     * 查询单个社区详情数据
     * @param cmmCommunityDto
     * @return
     */
    CmmCommunityDto queryCmmCtyDetail(CmmCommunityDto cmmCommunityDto);
}
