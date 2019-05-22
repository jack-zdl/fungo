package com.fungo.system.proxy;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.index.CardIndexBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

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

    CardIndexBean selectedGames();

}
