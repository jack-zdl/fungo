package com.fungo.games.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.games.entity.HomePage;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: Carlos
 * @Date: 2019/7/29
 */
@Service
public class GameHomeServiceImpl implements GameHomeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHomeService.class);


    @Autowired
    private FungoCacheIndex fungoCacheIndex;

    @Autowired
    private HomePageService homePageService;



    @Override
    public FungoPageResultDto<HomePage> index() {
        FungoPageResultDto<HomePage> re = new FungoPageResultDto<HomePage>();
        //查询有制顶标识的数据
        List<HomePage> topList =  homePageService.selectList(new EntityWrapper<HomePage>().eq("state",3));
        //查询正常的数据，并按修改时间排序
        EntityWrapper<HomePage> entityWrapper = new EntityWrapper<HomePage>();
        entityWrapper.eq("state",0);
        entityWrapper.orderBy("updated_at",false);
        List<HomePage> list =  homePageService.selectList(entityWrapper);
        if(!topList.isEmpty()){

        }
        re.setData(list);
        return re;
    }




    @Override
    public FungoPageResultDto<CardIndexBean> index(InputPageDto input, String os, String iosChannel, String app_channel, String appVersion) {

        FungoPageResultDto<CardIndexBean> re = null;
        //先从Redis获取
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_INDEX;
        String keySuffix = JSON.toJSONString(input) + os + iosChannel;
        if (StringUtils.isNotBlank(app_channel)) {
            keySuffix += app_channel;
        }
        re = (FungoPageResultDto<CardIndexBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }
        if (os == null) {
            os = "";
        }
        re = new FungoPageResultDto<>();
        List<CardIndexBean> clist = new ArrayList<CardIndexBean>();
        int page = input.getPage();

        //在ios平台下首页是否关闭部分功能
        boolean isCloseIndexSection = true;

        LOGGER.info("-----------在ios平台下首页是否关闭部分功能----os:{},------app_channel:{}------appVersion:{}-----isCloseIndexSection:{}", os, app_channel, appVersion,
                isCloseIndexSection);

        //int count = postService.selectCount(new EntityWrapper<CmmPost>().eq("type", 3));
        Page<CardIndexBean> pageBean = null;
        //第一页
        if (1 == page) {
            //ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {
                //本周精选(游戏)
                CardIndexBean hotGames = null;
                if (hotGames != null) {
                    clist.add(hotGames);
                }
            }
            //position 3 游戏攻略(精选文章)
            CardIndexBean postHost = null;
            if (postHost != null) {
                clist.add(postHost);
            }
            //position 4 安利墙
            //安利墙 ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {
                CardIndexBean anliWall = null;
                if (anliWall != null) {
                    clist.add(anliWall);
                }
            } else {
                ///position 5 精选文章 视频(position_code 0007)
                CardIndexBean postVidoe = null;
                if (postVidoe != null) {
                    clist.add(postVidoe);
                }
            }
            re.setAfter(2);
            re.setBefore(1);
            //第二页
        } else if (page == 2) {
            //安利墙
            //安利墙 ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {
                CardIndexBean postVidoe = null;
                if (postVidoe != null) {
                    clist.add(postVidoe);
                }
            }
            //精选文章 (视频)
            CardIndexBean postFine = null;
            if (postFine != null) {
                clist.add(postFine);
            }
            //大家都在玩
            //大家都在玩 ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {
                CardIndexBean selectedGames = null;
                if (selectedGames != null) {
                    clist.add(selectedGames);
                }
            }
            re.setAfter(3);
            re.setBefore(1);
        } else {
            page = page - 2;
            ArrayList<CardIndexBean> topicPosts = null;
            clist.addAll(topicPosts);
            if (topicPosts.size() == 0) {
                re.setAfter(-1);
            } else {
                re.setAfter(page + 3);
            }
        }
        re.setData(clist);
        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;
    }

}
