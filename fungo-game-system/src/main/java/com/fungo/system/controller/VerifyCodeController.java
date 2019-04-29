package com.fungo.system.controller;

import com.game.common.util.ImageUtil;
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
    @RequestMapping(value="/api/user/captcha")
	@ResponseBody
	public Map createImage(HttpServletResponse response, HttpSession session){
		response.setContentType("image/jpeg");
		//禁止图像缓存。
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String verifyCode = ImageUtil.generateVerifyCode(4);
		session.setAttribute("imageCode", verifyCode);
		 int w = 200, h = 80;  
//		 try {
//////			ImageUtil.outputImage(w, h, response.getOutputStream(), verifyCode);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
		 Map map = new HashMap();
		 map.put("imageCode",verifyCode);
		 map.put("sessionId", session.getId());
		 map.put("message", session.getAttribute("imageCode"));
		return map;
	}

}
