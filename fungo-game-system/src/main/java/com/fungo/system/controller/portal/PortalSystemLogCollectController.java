package com.fungo.system.controller.portal;


import com.fungo.system.dto.LogCollectInput;
import com.fungo.system.service.ISystemLogCollectService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *      app和web系统运行 | 用户操作日志收集controller
 *      绑定
 *      解绑
 * </p>
 * since:V2.4.6
 * @author mxf
 * @since 2018-12-04
 */
@Api(value = "", description = "日志收集")
@RestController
public class PortalSystemLogCollectController {

    @Autowired
    private ISystemLogCollectService iSystemLogCollectService;

    /**
     * app和web系统运行 | 用户操作日志收集
     * dataParam 参数：
     *              origin : 1-web, 2- android, 3-ios
     *              mb_id:      通过token获取
     *              runStack:   运行堆栈
     *              pageURL:    功能页面地址
     *              runEnvs:    运行环境数据
     * 有效版本范围：V2.4.6 ~
     * @throws Exception
     */
    @ApiOperation(value = "app和web系统运行 | 用户操作日志收集", httpMethod = "POST")
    @ApiImplicitParams({})
    @RequestMapping(value = "/api/portal/system/sys/log/collect", method = RequestMethod.POST)
    public ResultDto<String> bindThirdSNSWithLogged(@Anonymous MemberUserProfile memberprofile, @RequestBody LogCollectInput collectInput) throws Exception {
        String mb_id = "";
        if (null != memberprofile) {
            mb_id = memberprofile.getLoginId();
        }
        if (null != collectInput) {
            collectInput.setMb_id(mb_id);
        }
        iSystemLogCollectService.collectLog(collectInput);
        return ResultDto.success();
    }
}
