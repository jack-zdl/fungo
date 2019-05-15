package com.fungo.system.proxy.impl;

import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IMemeberProxyService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.enums.CommonEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/15
 */
@Service
public class MemeberProxyServiceImpl implements IMemeberProxyService {

    private static final Logger logger = LoggerFactory.getLogger(MemeberProxyServiceImpl.class);

    @Autowired
    private CommunityFeignClient communityFeignClient;

    @Autowired
    private GamesFeignClient gamesFeignClient;

    @Override
    public CmmPostDto selectCmmPost(String id) {
        CmmPostDto param = new CmmPostDto();
        param.setId(id);
        FungoPageResultDto<CmmPostDto> re = communityFeignClient.queryCmmPostList(param);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            param = re.getData().get(0);
        }
        return param;
    }
}
