package com.fungo.system.controller;


import com.fungo.system.service.ISysPushDeviceTokenService;
import com.game.common.dto.GameSysPushDeviceTokenInput;
import com.game.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 *     系统消息推送Controller
 * </p>
 * since:V2.4.6
 * @author mxf
 * @since 2018-12-04
 */
@RestController
public class SystemPushController {


    @Autowired
    private ISysPushDeviceTokenService iSysPushDeviceTokenService;


    /**
     * 消息推送目标设备token数据处理
     * @param gameSysPushDeviceTokenInput
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api/sys/device/token", method = RequestMethod.POST)
    public ResultDto<String> bindThirdSNSWithLogged(@Valid @RequestBody GameSysPushDeviceTokenInput gameSysPushDeviceTokenInput) throws Exception {

        if (null == gameSysPushDeviceTokenInput) {
            return ResultDto.error("-1", "请输入必填项参数");
        }
        boolean isAdd = iSysPushDeviceTokenService.addDeviceToken(gameSysPushDeviceTokenInput);
        if (isAdd) {
            return ResultDto.success("操作成功");
        }
        return ResultDto.error("-1", "操作失败");
    }


    //--------
}
