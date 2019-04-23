package com.game.common.util;


import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.FungoPageResultDto;

public class PageTools {
	public static void pageToResultDto(FungoPageResultDto fpage, Page page) {
		int count = page.getTotal();
		int pages = page.getPages();
		int current = page.getCurrent();
		if(current > pages) {
			fpage.setAfter(-1);
			fpage.setBefore(pages);
		}else {
			fpage.setBefore(current == 1?-1:current - 1);
			if(count != 0) {
				fpage.setAfter(current == pages?-1:current + 1);
			}else {
				fpage.setAfter(-1);
			}
		}
		fpage.setCount(count);
	}
	
//	public static void pageToResultDto(FungoPageResultDto fpage,List list) {
//		fpage.setAfter(-1);			
//		fpage.setBefore(-1);
//		fpage.setCount(list.size());
//	}
//
	

}
