package com.fungo.system.feign;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.game.GameInputPageDto;
import com.game.common.dto.game.GameItemInput;
import com.game.common.dto.game.GameOutBean;
import com.game.common.dto.game.GameOutPage;
import com.game.common.util.annotation.Anonymous;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p></p>
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@FeignClient(name = "FUNGO-GAME-GAMES")
public interface GamesFeignClient {

    @RequestMapping(value = "/api/content/gameList", method = RequestMethod.POST)
    public FungoPageResultDto<GameOutBean> getGameList( @RequestBody GameItemInput input);

    @RequestMapping(value = "/api/content/games", method = RequestMethod.POST)
    public FungoPageResultDto<GameOutPage> getGameList(@RequestBody GameInputPageDto gameInputDto);

    @RequestMapping(value = "/api/update/counter", method = RequestMethod.POST)
    boolean updateCounter(@RequestBody Map<String, String> map);
}
