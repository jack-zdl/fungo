package com.fungo.system.service;

import com.game.common.dto.ResultDto;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ITaskService {
    //旧版接口,已弃用
    public ResultDto<Map> getMineTaskInfo(String userId) throws IOException, ParseException;

    //旧版接口,已弃用
    public ResultDto<List> getTaskCategory();

    //旧版接口,已弃用
    public ResultDto<List> getTaskList(String userId, String categoryId) throws Exception;

    //public void getTaskHelp();


    //用户签到
    public ResultDto<Map> checkIn(String userId) throws Exception;

    //用户签到信息
    ResultDto<Map<String, Object>> checkInfo(String userId) throws Exception;

    //每日格言
    public ResultDto<String> DailyMotto() throws Exception;


}
