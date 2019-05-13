package com.fungo.games.feign;


import com.game.common.dto.AuthorBean;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.user.MemberOutBean;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
//迁移微服务后 SystemFeignClient调用 用户成长

    /**
     * 迁移微服务后 SystemFeignClient调用 用户成长
     * @param memberId
     * @param code
     * @param inectTaskVirtualCoinTaskCodeIdt
     * @param code1
     * @return
     */
    @RequestMapping(value="/api/member/exTask", method= RequestMethod.POST)
    Map<String, Object> exTask(@RequestParam("memberId")String memberId,@RequestParam("code") int code,@RequestParam("inectTaskVirtualCoinTaskCodeIdt") int inectTaskVirtualCoinTaskCodeIdt,@RequestParam("code1") int code1);


    /**
     * 根据用户id获取authorBean
     * @param memberId
     * @return
     */
    @RequestMapping(value="/api/member/getAuthor", method= RequestMethod.GET)
    AuthorBean getAuthor(String memberId);
}
