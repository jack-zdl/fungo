package com.fungo.system.mall.controller;


import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      商城日志接口
 * </p>
 *
 * @author mxf
 * @since 2019-01-18
 */
@RestController
public class MallLogsController {


    /**
     * 商城日志记录
     * @return
     */
    @PostMapping("/api/mall/logs")
    public ResultDto<String> getGoodsListForSeckill(MemberUserProfile memberUserPrefile) {

        String loginId = null;
        if (null != memberUserPrefile) {
            loginId = memberUserPrefile.getLoginId();
        }


        return ResultDto.error("-1", "日志记录出现异常");
    }





}
