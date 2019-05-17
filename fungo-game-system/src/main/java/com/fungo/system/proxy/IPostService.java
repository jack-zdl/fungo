package com.fungo.system.proxy;

import java.util.Set;

public interface IPostService {


	/**
	 * 查看用户在指定时间段内文章上推荐/置顶的文章数量
	 * @param mb_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Set<String> getArticleRecomAndTopCount(String mb_id, String startDate, String endDate);


}
