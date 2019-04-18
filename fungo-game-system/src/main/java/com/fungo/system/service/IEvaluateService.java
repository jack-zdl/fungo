package com.fungo.system.service;

import java.util.Set;

public interface IEvaluateService {

	/**
	 * 查看用户在指定时间段内游戏评论上热门和安利墙的评论数量
	 * @param mb_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Set<String> getGameEvaluationHotAndAnliCount(String mb_id, String startDate, String endDate);

}
