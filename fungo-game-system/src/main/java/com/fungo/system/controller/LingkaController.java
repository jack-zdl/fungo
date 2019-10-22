package com.fungo.system.controller;

import com.fungo.system.service.MemberPlayLogService;
import com.game.common.dto.ResultDto;
import com.game.common.vo.MemberPlayLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Lingka控制层</p>
 * @Author: dl.zhang
 * @Date: 2019/10/22
 */
@RestController
public class LingkaController {

    @Autowired
    private MemberPlayLogService memberPlayLogService;

    @PostMapping(value = "/api/system/member/log")
    public ResultDto<String> saveLingkaMemeberPlayLog(@Validated @RequestBody MemberPlayLogVO memberPlayLogVO){
        ResultDto<String> resultDto = null;
        resultDto = memberPlayLogService.saveMemberPalyLog( memberPlayLogVO);
        return  resultDto;
    }
}
