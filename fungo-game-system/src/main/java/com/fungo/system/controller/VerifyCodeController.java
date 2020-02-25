package com.fungo.system.controller;

import com.game.common.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class VerifyCodeController {


	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeController.class);

	@RequestMapping(value="/api/user/captcha")
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
			 LOGGER.error( "VerifyCodeController.createImage异常",e);
		}
	}

}
