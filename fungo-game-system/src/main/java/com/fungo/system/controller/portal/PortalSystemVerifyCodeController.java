package com.fungo.system.controller.portal;

import com.game.common.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class PortalSystemVerifyCodeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PortalSystemVerifyCodeController.class);

	/**
	 * 功能描述: pc端 请求图形验证码
	 * @param: [response, session]
	 * @return: void
	 * @auther: dl.zhang
	 * @date: 2019/5/27 11:47
	 */
    @RequestMapping(value="/api/portal/system/user/captcha")
	public void createImage(HttpServletResponse response, HttpSession session){
		response.setContentType("image/jpeg");
		//禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String verifyCode = ImageUtil.generateVerifyCode(4);
		session.setAttribute("imageCode", verifyCode);
		 int w = 200, h = 80;  
		 try {
			ImageUtil.outputImage(w, h, response.getOutputStream(), verifyCode);
		} catch (IOException e) {
			 LOGGER.error( "pc端请求图形验证码异常",e);
		}
	}

}
