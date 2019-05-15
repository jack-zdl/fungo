package com.fungo.games.service;



import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.evaluation.*;
import com.game.common.dto.game.MermberSearchInput;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IEvaluateService {

	public ResultDto<EvaluationBean> addGameEvaluation(String memberId, EvaluationInput commentInput)throws Exception;//评价游戏

	public ResultDto<EvaluationOutBean> getEvaluationDetail(String memberId, String evaluationId);//获取游戏评价详情

	public FungoPageResultDto<EvaluationOutPageDto> getEvaluationList(String memberId, EvaluationInputPageDto pagedto);//获取评价列表(默认获取两条回复)

	public ResultDto<EvaluationOutBean> anliEvaluationDetail(String memberId, String evaluationId);//安利墙游戏评价详情
//	根据后台标签id集合，分类标签，游戏id
	boolean feignAddGameTagInsert(List<String> tags, String categoryId, String gameId);

//	我的游戏评测(2.4.3)
    FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(String loginId, InputPageDto input) throws IOException;


    /**
	 * 查看用户在指定时间段内游戏评论上热门和安利墙的评论数量
	 * @param mb_id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
//	public Set<String> getGameEvaluationHotAndAnliCount(String mb_id, String startDate, String endDate);

}
