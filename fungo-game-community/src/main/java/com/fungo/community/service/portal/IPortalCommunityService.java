package com.fungo.community.service.portal;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CommunityInputPageDto;
import com.game.common.dto.community.CommunityOutPageDto;

/**
 * <p>
 *  PC2.0 社区业务新增
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/29 15:21
 */
public interface IPortalCommunityService {
    /**
     * PC2.0社区列表
     * @param userId
     * @param communityInputPageDto
     * @return
     */
    FungoPageResultDto<CommunityOutPageDto> getCmmCommunityList(String userId, CommunityInputPageDto communityInputPageDto);

    /**
     * PC2.0圈子首页列表
     * @param userId
     * @param communityInputPageDto
     * @return
     */
    FungoPageResultDto<CommunityOutPageDto> getCommunityListPC2_0(String userId, CommunityInputPageDto communityInputPageDto);
}
