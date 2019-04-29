package com.fungo.games.feign;


import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberOutBean;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author: lidf
 * @date: 2018/3/29
 * @description:
 * @version: 2.0
 */
@FeignClient(name = "FUNGO-GAME-SYSTEM")
public interface SystemFeignClient {

    @ApiOperation(value = "个人资料", notes = "用户身份校验(配合修改密码操作)")
    @RequestMapping(value = "/api/mine/info", method = RequestMethod.GET)
    public ResultDto<MemberOutBean> getUserInfo() throws Exception;

    @RequestMapping(value="/api/developer/test", method= RequestMethod.POST)
    public ResultDto<String> test();

}
