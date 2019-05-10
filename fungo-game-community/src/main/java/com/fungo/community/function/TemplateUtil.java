package com.fungo.community.function;


import org.springframework.web.servlet.ModelAndView;

public class TemplateUtil {
	
	
	//帖子内容网页模板
	public static ModelAndView returnPostHtmlTemplate(String origin, String title) {
		ModelAndView model = new ModelAndView();
		model.addObject("origin", origin);
		model.addObject("title", title);
		model.setViewName("tmp");
		
		return model;
	}
	
	
	public void mail() {
//		FreeMarkerTemplateUtils.processTemplateIntoString("ss", model)
	}
}
