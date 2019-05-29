package com.fungo.system.proxy.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IGameProxyService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCommentDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameInviteDto;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.enums.CommonEnum;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/13
 */
@Service
public class GameProxyServiceImpl implements IGameProxyService {

    private static final Logger logger = LoggerFactory.getLogger(GameProxyServiceImpl.class);

    @Autowired
    private CommunityFeignClient communityFeignClient;

    @Autowired
    private GamesFeignClient gamesFeignClient;

    /**
     * 功能描述:  根据主键获取社区帖子
     * @param: [id] 主键id
     * @return: com.game.common.dto.community.CmmPostDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:12
     */
//    @HystrixCommand(fallbackMethod = "hystrixSelectCmmPostById", ignoreExceptions = {Exception.class})
    @Override
    public CmmPostDto selectCmmPostById(CmmPostDto param) {
        CmmPostDto re = new CmmPostDto();
        FungoPageResultDto<CmmPostDto> cmmPostDtoFungoPageResultDto = communityFeignClient.queryCmmPostList(param);
        if (Integer.valueOf(CommonEnum.SUCCESS.code()).equals(cmmPostDtoFungoPageResultDto.getStatus()) && cmmPostDtoFungoPageResultDto.getData().size() > 0) {
            re = cmmPostDtoFungoPageResultDto.getData().get(0);
        }else
            logger.warn("GameProxyServiceImpl selectCmmPostById");
        return re;
    }

    /**
     * 功能描述: 根据主键查询社区一级评论
     * @param: [id] 社区一级评论主键id
     * @return: com.game.common.dto.community.CmmCommentDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:17
     */
//    @HystrixCommand(fallbackMethod = "hystrixSelectCmmCommentById", ignoreExceptions = {Exception.class}) //Unable to derive ExecutionIsolationStrategy from property value: Semaphore
    @Override
    public CmmCommentDto selectCmmCommentById(String id) {
        CmmCommentDto param = new CmmCommentDto();
        param.setId(id);
        FungoPageResultDto<CmmCommentDto> cmmCommentDtoFungoPageResultDto = communityFeignClient.queryFirstLevelCmtList(param);
        if (Integer.valueOf(CommonEnum.SUCCESS.code()).equals(cmmCommentDtoFungoPageResultDto.getStatus()) && cmmCommentDtoFungoPageResultDto.getData().size() > 0) {
            param = cmmCommentDtoFungoPageResultDto.getData().get(0);
        }else logger.warn("GameProxyServiceImpl selectCmmCommentById");
        return param;
    }

    /**
     * 功能描述: 根据主键查询游戏评价
     * @param: [id] 游戏评价主键
     * @return: com.game.common.dto.game.GameEvaluationDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:26
     */
//    @HystrixCommand(fallbackMethod = "hystrixSelectGameEvaluationById", ignoreExceptions = {Exception.class})
    @Override
    public GameEvaluationDto selectGameEvaluationById(GameEvaluationDto gameEvaluationDto) {
        GameEvaluationDto re = new GameEvaluationDto();
        FungoPageResultDto<GameEvaluationDto> gameEvaluationDtoPage = gamesFeignClient.getGameEvaluationPage(gameEvaluationDto);
        if (null != gameEvaluationDtoPage) {
            List<GameEvaluationDto> gameEvaluationDtoList = gameEvaluationDtoPage.getData();
            if (null != gameEvaluationDtoList && !gameEvaluationDtoList.isEmpty()) {
                re = gameEvaluationDtoList.get(0);
            }
        }
        return re;
    }

    /**
     * 功能描述: 根据游戏主键查询游戏
     * @param: [id] 游戏主键
     * @return: com.game.common.dto.GameDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:30
     */
//    @HystrixCommand(fallbackMethod = "hystrixSelectGameById", ignoreExceptions = {Exception.class})
    @Override
    public GameDto selectGameById(GameDto param) {
        GameDto re = new GameDto();
        FungoPageResultDto<GameDto> gamePage = gamesFeignClient.getGamePage(param);
        if (null != gamePage) {
            List<GameDto> gameDtoList = gamePage.getData();
            if (null != gameDtoList && !gameDtoList.isEmpty()) {
                re = gameDtoList.get(0);
            }
        }

        return re;
    }

    /**
     * 功能描述: 根据心情主键查询心情
     * @param: [id] 心情主键
     * @return: com.game.common.dto.community.MooMoodDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:34
     */
//    @HystrixCommand(fallbackMethod = "hystrixSelectMooMoodById", ignoreExceptions = {Exception.class})
    @Override
    public MooMoodDto selectMooMoodById(String id) {
        MooMoodDto param = new MooMoodDto();
        param.setId(id);
        FungoPageResultDto<MooMoodDto> re = communityFeignClient.queryCmmMoodList(param);
        if (Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0) {
            return re.getData().get(0);
        }else logger.warn("GameProxyServiceImpl selectMooMoodById");
        return null;
    }

    /**
     *
     * 功能描述: 根据心情评论主键查询心情评论
     * @param: [id] 心情评论主键
     * @return: com.game.common.dto.community.MooMessageDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:37
     */
//    @HystrixCommand(fallbackMethod = "hystrixSelectMooMessageById", ignoreExceptions = {Exception.class})
    @Override
    public MooMessageDto selectMooMessageById(String id) {
        MooMessageDto mooMessageDto = new MooMessageDto();
        mooMessageDto.setId(id);
        FungoPageResultDto<MooMessageDto> re = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
        if (Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0) {
            return re.getData().get(0);
        }else  logger.warn("GameProxyServiceImpl selectMooMessageById");
        return mooMessageDto;
    }


    @Override
    public int getGameSelectCountByLikeNameAndState(GameDto gameDto) {
        return gamesFeignClient.getGameSelectCountByLikeNameAndState(gameDto);
    }

    //    getEvaluationEntityWrapper
//    @HystrixCommand(fallbackMethod = "hystrixGetEvaluationEntityWrapper", ignoreExceptions = {Exception.class})
    @Override
    public List<GameEvaluationDto> getEvaluationEntityWrapper(String memberId, String startDate, String endDate) {
        ResultDto<List<GameEvaluationDto>> evaluationEntityWrapper = gamesFeignClient.getEvaluationEntityWrapper(memberId, startDate, endDate);
        return evaluationEntityWrapper.getData();
    }

//    @HystrixCommand(fallbackMethod = "hystrixSelectGameInvite", ignoreExceptions = {Exception.class})
    @Override
    public GameInviteDto selectGameInvite(GameInviteDto gameInviteDto) {
        GameInviteDto result = new GameInviteDto();
        FungoPageResultDto<GameInviteDto> re = gamesFeignClient.getGameInvitePage(gameInviteDto);
        if (Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0) {
            result = re.getData().get(0);
        }else  logger.warn("GameProxyServiceImpl selectGameInvite");
        return result;
    }

    @Override
    public List<Map<String, Object>> getEvaluationFeeds(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<String> getRecommendMembersFromEvaluation(Integer x, Integer y, List<String> wathMbsSet) {
        ResultDto<List<String>> re = gamesFeignClient.getRecommendMembersFromEvaluation(x, y, wathMbsSet);
        return re.getData();
    }

    @Override
    public CardIndexBean selectedGames() {
        return gamesFeignClient.selectedGames();
    }

    @Override
    public List<GameEvaluationDto> selectGameEvaluationPage() {
        FungoPageResultDto<GameEvaluationDto> re = gamesFeignClient.selectGameEvaluationPage();
        if (Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0) {
            return re.getData();
        }else logger.warn("GameProxyServiceImpl selectGameEvaluationPage");
        return new ArrayList<>();
    }

    @Override
    public List<Map> getHonorQualificationOfEssenceEva() {
        ResultDto<List<Map>> re = gamesFeignClient.getUserGameReviewBoutiqueNumber();
        if (re.isSuccess() && re.getData() != null && re.getData().size() > 0) {
            return re.getData();
        }
        return new ArrayList<>();
    }

    public List<GameEvaluationDto> hystrixGetEvaluationEntityWrapper(String memberId, String startDate, String endDate) {
        logger.warn("IndexProxyServiceImpl.hystrixGetEvaluationEntityWrapper ");
        return new CopyOnWriteArrayList<>();
    }

    public CmmPostDto hystrixSelectCmmPostById(CmmPostDto param) {
        logger.warn("GameProxyServiceImpl.selectCmmPostById根据主键获取社区帖子异常");
        return new CmmPostDto();
    }

    public CmmCommentDto hystrixSelectCmmCommentById(String id) {
        logger.warn("GameProxyServiceImpl.selectCmmCommentById根据主键查询社区一级评论异常");
        return new CmmCommentDto();
    }

    public GameEvaluationDto hystrixSelectGameEvaluationById(GameEvaluationDto gameEvaluationDto) {
        logger.warn("GameProxyServiceImpl.selectGameEvaluationById根据主键查询游戏评价异常");
        return new GameEvaluationDto();
    }

    public GameDto hystrixSelectGameById(GameDto param) {
        logger.warn("GameProxyServiceImpl.selectGameById根据主键查询游戏评价异常");
        return new GameDto();
    }

    public MooMoodDto hystrixSelectMooMoodById(String id) {
        logger.warn("GameProxyServiceImpl.selectMooMoodById根据主键查询游戏评价异常");
        return new MooMoodDto();
    }

    public MooMessageDto hystrixSelectMooMessageById(String id) {
        logger.warn("GameProxyServiceImpl.selectMooMessageById根据主键查询游戏评价异常");
        return new MooMessageDto();
    }

    public GameInviteDto hystrixSelectGameInvite(GameInviteDto gameInviteDto) {
        logger.warn("GameProxyServiceImpl.hystrixSelectGameInvite根据主键查询游戏评价异常");
        return new GameInviteDto();
    }
}
