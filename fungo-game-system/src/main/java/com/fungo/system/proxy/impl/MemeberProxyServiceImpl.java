package com.fungo.system.proxy.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IMemeberProxyService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMoodDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameSurveyRelDto;
import com.game.common.dto.game.ReplyDto;
import com.game.common.enums.CommonEnum;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/15
 */
@Service
public class MemeberProxyServiceImpl implements IMemeberProxyService {

    private static final Logger logger = LoggerFactory.getLogger(MemeberProxyServiceImpl.class);

    @Autowired
    private CommunityFeignClient communityFeignClient;

    @Autowired
    private GamesFeignClient gamesFeignClient;

    /**
     * 功能描述: 根据主键获取社区帖子
     * @param: [id] 社区主键
     * @return: com.game.common.dto.community.CmmPostDto
     * @auther: dl.zhang
     * @date: 2019/5/15 16:03
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectCmmPost",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmPostDto selectCmmPost(String id) {
        CmmPostDto param = new CmmPostDto();
        param.setId(id);
        FungoPageResultDto<CmmPostDto> re = communityFeignClient.queryCmmPostList(param);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            param = re.getData().get(0);
        }else {
            logger.warn("MemeberProxyServiceImpl.selectCmmPost 异常");
        }
        return param;
    }

    /**
     * 功能描述: 分页查询游戏测试会员关联表
     * @param page
     * @param limit
     * @param memberId
     * @param status
     * @return: com.baomidou.mybatisplus.plugins.Page<com.game.common.dto.game.GameSurveyRelDto>
     * @auther: dl.zhang
     * @date: 2019/5/15 16:06
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectGameSurveyRelPage",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public FungoPageResultDto<GameSurveyRelDto> selectGameSurveyRelPage(int page, int limit, String memberId, int status) {
        GameSurveyRelDto gameSurveyRelDto = new GameSurveyRelDto();
        gameSurveyRelDto.setMemberId(memberId);
        gameSurveyRelDto.setState(status);
        gameSurveyRelDto.setPage(page);
        gameSurveyRelDto.setLimit(limit);
        return gamesFeignClient.getGameSurveyRelPage(gameSurveyRelDto);
    }

    /**
     * 功能描述: 根据心情参数获取总数
     * @param: mooMoodDto
     * @return: int
     * @auther: dl.zhang
     * @date: 2019/5/15 16:14
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectMooMoodCount",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public int selectMooMoodCount(MooMoodDto mooMoodDto) {
        ResultDto<Integer> re =  communityFeignClient.queryCmmMoodCount(mooMoodDto);
        if(re.isSuccess()){
            return re.getData();
        }else {
            logger.warn("根据心情参数获取总数异常");
            return 0;
        }
    }
    
    /**
     * 功能描述: 根据心情参数分页查询
     * @param: mooMoodDto
     * @return: com.baomidou.mybatisplus.plugins.Page<com.game.common.dto.community.MooMoodDto>
     * @auther: dl.zhang
     * @date: 2019/5/15 16:17
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectMooMoodPage",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public Page<MooMoodDto> selectMooMoodPage(MooMoodDto mooMoodDto) {
        Page<MooMoodDto> mooMoodPage = new Page<>();
        FungoPageResultDto<MooMoodDto> re = communityFeignClient.queryCmmMoodList(mooMoodDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            mooMoodPage.setRecords(re.getData());
            mooMoodPage.setTotal(re.getCount());
        }else
            logger.warn("MemeberProxyServiceImpl.selectMooMoodPage 异常");
        return mooMoodPage;
    }

    /**
     * 功能描述: 查询回复总数
     * @param: replyDto
     * @return: int
     * @auther: dl.zhang
     * @date: 2019/5/15 16:17
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectReplyCount",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public int selectReplyCount(CmmCmtReplyDto replyDto) {
        ResultDto<Integer> re = communityFeignClient.querySecondLevelCmtCount(replyDto);
        return re.getData();
    }

    /**
     * 功能描述: 根据主键查询回复
     * @param: replyDto
     * @return: com.game.common.dto.game.ReplyDto
     * @auther: dl.zhang
     * @date: 2019/5/15 16:18
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectReplyById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmCmtReplyDto selectReplyById(CmmCmtReplyDto replyDto) {
        CmmCmtReplyDto reply =null;
        FungoPageResultDto<CmmCmtReplyDto>  re = communityFeignClient.querySecondLevelCmtList(replyDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            reply =  re.getData().get(0);
        }else
            logger.warn("MemeberProxyServiceImpl.selectReplyById 异常");
        return reply;
    }

    /**
     * 功能描述: 分页查询游戏评价
     * @param: gameEvaluationDto
     * @return: com.baomidou.mybatisplus.plugins.Page<com.game.common.dto.game.GameEvaluationDto>
     * @auther: dl.zhang
     * @date: 2019/5/15 16:19
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectGameEvaluationPage",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public  Page<GameEvaluationDto>  selectGameEvaluationPage(GameEvaluationDto gameEvaluationDto) {
        Page<GameEvaluationDto> page = new Page<>();
        FungoPageResultDto<GameEvaluationDto> resultDto = gamesFeignClient.getGameEvaluationPage(gameEvaluationDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(resultDto.getStatus()) && resultDto.getData().size() > 0){
            page.setRecords(resultDto.getData());
        }else
            logger.warn("MemeberProxyServiceImpl.selectGameEvaluationPage 异常");
        return page;
    }

    /**
     * 功能描述: 分页查询社区帖子
     * @param: cmmPostDto
     * @return: com.baomidou.mybatisplus.plugins.Page<com.game.common.dto.community.CmmPostDto>
     * @auther: dl.zhang
     * @date: 2019/5/15 16:19
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectCmmPostpage",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public Page<CmmPostDto> selectCmmPostpage(CmmPostDto cmmPostDto) {
        Page<CmmPostDto> page = new Page<CmmPostDto>();
        FungoPageResultDto<CmmPostDto> cmmPostDtoFungoPageResultDto = communityFeignClient.queryCmmPostList(cmmPostDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(cmmPostDtoFungoPageResultDto.getStatus()) && cmmPostDtoFungoPageResultDto.getData().size() > 0){
            page.setRecords(cmmPostDtoFungoPageResultDto.getData());
        }else
            logger.warn("MemeberProxyServiceImpl.selectCmmPostpage 异常");
        return page;
    }

    /**
     * 功能描述: 根据主键查询社区
     * @param: cmmCommunityDto
     * @return: com.game.common.dto.community.CmmCommunityDto
     * @auther: dl.zhang
     * @date: 2019/5/15 16:20
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectCmmCommunityById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmCommunityDto selectCmmCommunityById(CmmCommunityDto cmmCommunityDto) {
        CmmCommunityDto communityDto = new CmmCommunityDto();
        FungoPageResultDto<CmmCommunityDto> re = communityFeignClient.queryCmmPostList(cmmCommunityDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            communityDto = re.getData().get(0);
        }else
            logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById 异常");
        return communityDto;
    }

    @Override
    public int CmmPostCount(CmmPostDto cmmPostDto) {
        ResultDto<Integer> re = communityFeignClient.queryCmmPostCount(cmmPostDto);
        return re.getData();
    }


    public CmmPostDto hystrixSelectCmmPost(String id) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return new CmmPostDto();
    }


    public FungoPageResultDto<GameSurveyRelDto> hystrixSelectGameSurveyRelPage(int page, int limit, String memberId, int status) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return new FungoPageResultDto<>();
    }

    public int hystrixSelectMooMoodCount(MooMoodDto mooMoodDto) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return 0;
    }

    public Page<MooMoodDto> hystrixSelectMooMoodPage(MooMoodDto mooMoodDto) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return new Page<>();
    }

    public int hystrixSelectReplyCount(ReplyDto replyDto) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return 0;
    }

    public CmmCmtReplyDto hystrixSelectReplyById(ReplyDto replyDto) {
        return new CmmCmtReplyDto();
    }

    public Page<GameEvaluationDto> hystrixSelectGameEvaluationPage(GameEvaluationDto gameEvaluationDto) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return new Page<>();
    }


    public Page<CmmPostDto> hystrixSelectCmmPostpage(CmmPostDto cmmPostDto) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return new Page<>();
    }

    public CmmCommunityDto hystrixSelectCmmCommunityById(CmmCommunityDto cmmCommunityDto) {
        logger.warn("MemeberProxyServiceImpl.selectCmmCommunityById根据主键获取社区帖子异常");
        return new CmmCommunityDto();
    }
}
