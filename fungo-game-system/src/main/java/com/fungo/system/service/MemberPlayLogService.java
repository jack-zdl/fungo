package com.fungo.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.entity.Member;
import com.game.common.dto.ResultDto;
import com.game.common.vo.MemberPlayLogVO;

import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/22
 */
public interface MemberPlayLogService {

    ResultDto<String> saveMemberPalyLog(Map map);
}
