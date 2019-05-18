package com.fungo.system.proxy;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/18
 */
public interface IndexProxyService {

    CmmPostDto selctCmmPostOne(CmmPostDto cmmPostDto);


    HashMap<String, BigDecimal>  getRateData(String id);

    Page<CmmPostDto> selectCmmPostPage(CmmPostDto cmmPostDto);
    CmmCommunityDto selectCmmCommuntityDetail(CmmCommunityDto cmmCommunityDto);
}
