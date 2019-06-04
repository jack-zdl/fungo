package com.fungo.system.facede.impl;


import com.fungo.system.facede.IPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import java.util.*;


@EnableAsync
@Service
public class PostServiceImpl implements IPostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);


    @Override
    public Set<String> getArticleRecomAndTopCount(String mb_id, String startDate, String endDate) {
        return null;
    }
}