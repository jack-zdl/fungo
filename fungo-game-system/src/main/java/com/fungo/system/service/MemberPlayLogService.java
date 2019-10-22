package com.fungo.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.system.entity.Member;
import com.game.common.dto.ResultDto;
import com.game.common.vo.MemberPlayLogVO;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/10/22
 */
public interface MemberPlayLogService {

    ResultDto<String> saveMemberPalyLog(MemberPlayLogVO memberPlayLogVO);
}
