package com.fungo.system.proxy.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IGameProxyService;
import com.fungo.system.service.impl.MemberIncentRiskServiceImpl;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.community.CmmCommentDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.enums.CommonEnum;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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

//    /**
//     * 功能描述:
//     * @param: [map]
//     * @return: java.lang.String
//     * @auther: dl.zhang
//     * @date: 2019/5/14 11:12
//     */
//    @HystrixCommand(fallbackMethod = "hystrixGetMemberIdByTargetId",ignoreExceptions = {Exception.class},
//            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
//    @Override
//    public String getMemberIdByTargetId(Map<String, String> map) {
//        return communityFeignClient.getMemberIdByTargetId(map);
//    }
//
//    public String hystrixGetMemberIdByTargetId(Map<String, String> map){
//        logger.warn("GameProxyServiceImpl.getMemberIdByTargetId获取被点赞用户的id异常");
//        return null;
//    }
    
    /**
     * 功能描述:  根据主键获取社区帖子
     * @param: [id] 主键id
     * @return: com.game.common.dto.community.CmmPostDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:12
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectCmmPostById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmPostDto selectCmmPostById(String id) {
        CmmPostDto param = new CmmPostDto();
        param.setId(id);
        FungoPageResultDto<CmmPostDto> cmmPostDtoFungoPageResultDto = communityFeignClient.queryCmmPostList(param);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(cmmPostDtoFungoPageResultDto.getStatus()) && cmmPostDtoFungoPageResultDto.getData().size() > 0){
            param = cmmPostDtoFungoPageResultDto.getData().get(0);
        }
        return param;
    }

    public CmmPostDto hystrixSelectCmmPostById(String id){
        logger.warn("GameProxyServiceImpl.selectCmmPostById根据主键获取社区帖子异常");
        return null;
    }

    /**
     * @todo 社区没有接口
     * 功能描述: 根据主键查询社区一级评论
     * @param: [id] 社区一级评论主键id
     * @return: com.game.common.dto.community.CmmCommentDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:17
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectCmmCommentById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmCommentDto selectCmmCommentById(String id) {
        return null;
    }



    public CmmCommentDto hystrixSelectCmmCommentById(){
        logger.warn("GameProxyServiceImpl.selectCmmCommentById根据主键查询社区一级评论异常");
        return null;
    }

    /**
     * 功能描述: 根据主键查询游戏评价
     * @param: [id] 游戏评价主键
     * @return: com.game.common.dto.game.GameEvaluationDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:26
     */
     @HystrixCommand(fallbackMethod = "hystrixSelectGameEvaluationById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public GameEvaluationDto selectGameEvaluationById(String id) {
         GameEvaluationDto param = new GameEvaluationDto();
         param.setId(id);
         Page<GameEvaluationDto> gameEvaluationDtoPage = gamesFeignClient.getGameEvaluationPage(param);
         if(gameEvaluationDtoPage.getRecords().size() > 0){
             param = gameEvaluationDtoPage.getRecords().get(0);
         }
        return param;
    }


    public GameEvaluationDto hystrixSelectGameEvaluationById(String id){
        logger.warn("GameProxyServiceImpl.selectGameEvaluationById根据主键查询游戏评价异常");
        return null;
    }

    
    /**
     * 功能描述: 根据游戏主键查询游戏
     * @param: [id] 游戏主键
     * @return: com.game.common.dto.GameDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:30
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectGameById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public GameDto selectGameById(String id) {
        GameDto param = new GameDto();
        param.setId(id);
        Page<GameDto> gamePage =   gamesFeignClient.getGamePage(param);
        if(gamePage.getRecords().size() > 0 ){
            param = gamePage.getRecords().get(0);
        }
        return param;
    }


    public GameDto hystrixSelectGameById(String id){
        return null;
    }

    /**
     * 功能描述: 根据心情主键查询心情
     * @param: [id] 心情主键
     * @return: com.game.common.dto.community.MooMoodDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:34
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectMooMoodById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public MooMoodDto selectMooMoodById(String id) {
        MooMoodDto param = new MooMoodDto();
        param.setId(id);
        FungoPageResultDto<MooMoodDto> re = communityFeignClient.queryCmmMoodList(param);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(re.getStatus()) && re.getData().size() > 0){
            param = re.getData().get(0);
        }
        return param;
    }

    public MooMoodDto hystrixSelectMooMoodById(String id) {
        return null;
    }

    /**
     * @todo 心情评论
     * 功能描述: 根据心情评论主键查询心情评论
     * @param: [id] 心情评论主键
     * @return: com.game.common.dto.community.MooMessageDto
     * @auther: dl.zhang
     * @date: 2019/5/14 11:37
     */
    @HystrixCommand(fallbackMethod = "hystrixSelectMooMessageById",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public MooMessageDto selectMooMessageById(String id) {
        return null;
    }

    public MooMessageDto hystrixSelectMooMessageById(String id) {
        return null;
    }




}
