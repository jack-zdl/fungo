package com.fungo.system.facede;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.index.CardIndexBean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/18
 */
public interface IndexProxyService {

    CmmPostDto selctCmmPostOne(CmmPostDto cmmPostDto);


    Map<String, BigDecimal> getRateData(String id);

    Page<CmmPostDto> selectCmmPostPage(CmmPostDto cmmPostDto);
    CmmCommunityDto selectCmmCommuntityDetail(CmmCommunityDto cmmCommunityDto);

    CardIndexBean selectedGames();


    Map getGameMsgByPost(CmmPostDto cmmPost);
}
