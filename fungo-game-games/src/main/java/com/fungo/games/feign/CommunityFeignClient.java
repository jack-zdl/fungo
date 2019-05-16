package com.fungo.games.feign;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.game.ReplyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 *
 * @Author lyc
 * @create 2019/5/7 10:26
 */
@FeignClient(name = "FUNGO-GAME-COMMUNITY")
@RequestMapping("/ms/service/cmm")
public interface CommunityFeignClient {

    /**
     * 根据条件判断获取ReplyDtoList集合
     * @param replyDto
     * @return
     */
    @RequestMapping(value="/cmt/s/lists", method= RequestMethod.POST)
    FungoPageResultDto<CmmCmtReplyDto>  getReplyDtoBysSelectPageOrderByCreatedAt(@RequestBody CmmCmtReplyDto replyDto);

    /**
     * 根据id获取cmmcomunity单个对象
     * @param cmmCommunityDto
     * @return
     */
    @RequestMapping(value="/cty/lists", method= RequestMethod.POST)
    FungoPageResultDto<CmmCommunityDto> getCmmCommunitySelectOneById(@RequestBody CmmCommunityDto cmmCommunityDto);
}
