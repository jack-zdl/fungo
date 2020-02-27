package com.fungo.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.entity.BasAction;
import com.game.common.dto.ResultDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * <p>
 * 动作 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-05-07
 */
public interface IBasActionService  {

    ResultDto<Map<String,Object>> getCollectByGame(String gameId, String memberId);
}
