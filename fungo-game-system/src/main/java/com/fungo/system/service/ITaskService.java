package com.fungo.system.service;

import com.game.common.dto.ResultDto;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ITaskService {

    //旧版接口,已弃用
    ResultDto<Map> getMineTaskInfo(String userId) throws IOException, ParseException;

    //旧版接口,已弃用
    ResultDto<List> getTaskCategory();

    //旧版接口,已弃用
    ResultDto<List> getTaskList(String userId, String categoryId) throws Exception;

    //用户签到
    ResultDto<Map> checkIn(String userId) throws Exception;

    //用户签到信息
    ResultDto<Map<String, Object>> checkInfo(String userId) throws Exception;

    //每日格言
    ResultDto<String> DailyMotto(String userId) throws Exception;

    ResultDto<String> followUser(String userId) throws Exception;

    ResultDto<String> openPush(String userId);


    ResultDto<String> taskCheckUserFollowOfficialUser(String userId);

    ResultDto<String> taskCheckUserFollowOfficialCircle(String userId);

    ResultDto<String> taskCheckUserBindQQWeiboWechat(String userId);
}
