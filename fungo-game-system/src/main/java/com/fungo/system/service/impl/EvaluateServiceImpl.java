package com.fungo.system.service.impl;

import com.fungo.system.service.IEvaluateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EvaluateServiceImpl implements IEvaluateService {


    private static final Logger logger = LoggerFactory.getLogger(EvaluateServiceImpl.class);

    @Override
    public Set<String> getGameEvaluationHotAndAnliCount(String mb_id, String startDate, String endDate) {
//        Set<String> gameEvaSet = null;
//        try {
//            EntityWrapper<GameEvaluation> evaluationEntityWrapper = new EntityWrapper<>();
//            evaluationEntityWrapper.eq("member_id", mb_id);
//            evaluationEntityWrapper.between("updated_at", startDate, endDate);
//            evaluationEntityWrapper.eq("state", 0);
//            //type  0:普通 1:热门 2:精华
//            evaluationEntityWrapper.in("type", new Integer[]{1, 2});
//
//            List<GameEvaluation> gameEvaluationsList = gameEvaluationService.selectList(evaluationEntityWrapper);
//            if (null != gameEvaluationsList && !gameEvaluationsList.isEmpty()) {
//
//                gameEvaSet = new HashSet<String>();
//
//                for (GameEvaluation gameEvaluation : gameEvaluationsList) {
//                    gameEvaSet.add(gameEvaluation.getId());
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            logger.error("查看用户在指定时间段内游戏评论上热门和安利墙的文章数量出现异常", ex);
//        }
//        logger.info("查看用户在指定时间段内游戏评论上热门和安利墙的文章数量-gameEvaSet:{}", gameEvaSet);
//        return gameEvaSet;
        return null;
    }

}
