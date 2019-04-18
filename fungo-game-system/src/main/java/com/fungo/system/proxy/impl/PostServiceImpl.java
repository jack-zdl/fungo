package com.fungo.system.proxy.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.api.FungoPageResultDto;
import com.fungo.api.ResultDto;
import com.fungo.api.vd.StreamInfo;
import com.fungo.emoji.EmojiDealUtil;
import com.fungo.emoji.FilterEmojiUtil;
import com.fungo.framework.SerUtils;
import com.fungo.framework.Setting;
import com.fungo.framework.dao.PageTools;
import com.fungo.framework.exceptions.BusinessException;
import com.fungo.framework.video.IVideoService;
import com.fungo.system.proxy.IPostService;
import com.fungo.tools.DateTools;
import com.fungo.validate.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@EnableAsync
@Service
public class PostServiceImpl implements IPostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);


    @Override
    public Set<String> getArticleRecomAndTopCount(String mb_id, String startDate, String endDate) {
        return null;
    }
}