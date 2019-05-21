package com.fungo.system.proxy.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IndexProxyService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.enums.CommonEnum;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/18
 */
@Service
public class IndexProxyServiceImpl implements IndexProxyService {

    private static final Logger logger = LoggerFactory.getLogger(GameProxyServiceImpl.class);

    @Autowired
    private CommunityFeignClient communityFeignClient;

    @Autowired
    private GamesFeignClient gamesFeignClient;

    @HystrixCommand(fallbackMethod = "hystrixSelctCmmPostOne",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmPostDto selctCmmPostOne(CmmPostDto cmmPostDto) {
        CmmPostDto re = new CmmPostDto();
        FungoPageResultDto<CmmPostDto> cmmPostDtoFungoPageResultDto = communityFeignClient.queryCmmPostList(cmmPostDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(cmmPostDtoFungoPageResultDto.getStatus()) && cmmPostDtoFungoPageResultDto.getData().size() > 0){
            re = cmmPostDtoFungoPageResultDto.getData().get(0);
        }
        return re;
    }

    @HystrixCommand(fallbackMethod = "hystrixGetRateData",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public HashMap<String, BigDecimal> getRateData(String id) {
        // @todo
        ResultDto<HashMap<String, BigDecimal>> resultDto = gamesFeignClient.getRateData(id);
        if(resultDto.isSuccess()){
            return resultDto.getData();
        }
        return null;
    }

    @HystrixCommand(fallbackMethod = "hystrixSelectCmmPostPage",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public Page<CmmPostDto> selectCmmPostPage(CmmPostDto cmmPostDto) {
        Page<CmmPostDto> page = new Page<>();
        FungoPageResultDto<CmmPostDto> cmmPostDtoFungoPageResultDto = communityFeignClient.listCmmPostTopicPost(cmmPostDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(cmmPostDtoFungoPageResultDto.getStatus()) && cmmPostDtoFungoPageResultDto.getData().size() > 0){
            page.setRecords(cmmPostDtoFungoPageResultDto.getData());
            page.setTotal(cmmPostDtoFungoPageResultDto.getData().size());
        }
        return page;
    }

    @HystrixCommand(fallbackMethod = "hystrixSelectCmmCommuntityDetail",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CmmCommunityDto selectCmmCommuntityDetail(CmmCommunityDto cmmCommunityDto) {
        CmmCommunityDto re = new CmmCommunityDto();
        FungoPageResultDto<CmmCommunityDto>  cmmCommunityDtoFungoPageResultDto =  communityFeignClient.queryCmmCtyDetail(cmmCommunityDto);
        if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(cmmCommunityDtoFungoPageResultDto.getStatus()) && cmmCommunityDtoFungoPageResultDto.getData().size() > 0){
            re = cmmCommunityDtoFungoPageResultDto.getData().get(0);
        }
        return re;
    }

    @HystrixCommand(fallbackMethod = "hystrixSelectedGames",ignoreExceptions = {Exception.class},
            commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
    @Override
    public CardIndexBean selectedGames() {
        ResultDto<CardIndexBean>  cardIndexBeanResultDto = gamesFeignClient.getSelectedGames();
        return cardIndexBeanResultDto.getData();
    }

    public CmmPostDto hystrixSelctCmmPostOne(CmmPostDto cmmPostDto) {
        logger.warn("IndexProxyServiceImpl.hystrixSelctCmmPostOne ");
        return null;
    }

    public HashMap<String, BigDecimal> hystrixGetRateData(String id) {
        logger.warn("IndexProxyServiceImpl.hystrixGetRateData ");
        return null;
    }

    public Page<CmmPostDto> hystrixSelectCmmPostPage(CmmPostDto cmmPostDto) {
        logger.warn("IndexProxyServiceImpl.hystrixSelectCmmPostPage ");
        return null;
    }

    public CmmCommunityDto hystrixSelectCmmCommuntityDetail(CmmCommunityDto cmmCommunityDto) {
        logger.warn("IndexProxyServiceImpl.hystrixSelectCmmCommuntityDetail ");
        return null;
    }

    public CardIndexBean hystrixSelectedGames() {
        logger.warn("IndexProxyServiceImpl.hystrixSelectedGames ");
        return null;
    }
}
