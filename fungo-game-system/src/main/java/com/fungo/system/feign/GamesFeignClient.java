package com.fungo.system.feign;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.game.GameItemInput;
import com.game.common.dto.game.GameOutBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * <p></p>
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@FeignClient(name = "FUNGO-GAME-GAMES")
public interface GamesFeignClient {

    @RequestMapping(value = "/api/content/gameList", method = RequestMethod.POST)
    public FungoPageResultDto<GameOutBean> getGameList( @RequestBody GameItemInput input);
}
