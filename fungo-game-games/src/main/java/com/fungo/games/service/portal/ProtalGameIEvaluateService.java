package com.fungo.games.service.portal;

import com.game.common.dto.ResultDto;
import com.game.common.dto.evaluation.*;

public interface ProtalGameIEvaluateService {

	ResultDto<EvaluationOutBean> anliEvaluationDetail(String memberId, String evaluationId);//安利墙游戏评价详情

}
