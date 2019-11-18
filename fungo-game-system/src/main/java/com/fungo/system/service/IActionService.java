package com.fungo.system.service;

import com.game.common.dto.ActionInput;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.BannerBean;
import com.game.common.dto.mall.MallBannersInput;
import com.game.common.dto.mall.MallGoodsInput;

import java.util.HashMap;

public interface IActionService {
    public ResultDto<String> like(String memberId, ActionInput inputDto, String appVersion)throws Exception;//点赞
    public ResultDto<String>  unLike(String memberId,ActionInput inputDto)throws Exception;//取消点赞
    public ResultDto<String>  share(String memberId,ActionInput inputDto)throws Exception;//分享
    public ResultDto<String>  collect(String memberId,ActionInput inputDto)throws Exception;//收藏
    public ResultDto<String>  unCollect(String memberId,ActionInput inputDto)throws Exception;//取消收藏
    public ResultDto<String>  follow(String memberId,ActionInput inputDto)throws Exception;//关注
    public ResultDto<String>  unFollow(String memberId,ActionInput inputDto)throws Exception;//取消关注
    public ResultDto<String>  report(String memberId,ActionInput inputDto)throws Exception;//举报
    public ResultDto<String> collectionLike(String memberId, ActionInput inputDto, String appVersion)throws Exception;//合集点赞
    public ResultDto<String>  unCollectionLike(String memberId,ActionInput inputDto)throws Exception;//合集取消点赞
    public ResultDto<BannerBean>  queryCollectionLike(MallBannersInput mallBannersInput)throws Exception;//查询合集点赞总数



    /**
     *  游戏包下载次数记录
     *  注意：一个设备上，一个游戏下载多次，记录为有效次数为一次。
     * @param memberId
     * @param inputDto
     * @return
     * @throws Exception
     */
    public ResultDto<String>  downLoad(String memberId,ActionInput inputDto)throws Exception;//下载
    public ResultDto<String>  ignore(String memberId,ActionInput inputDto)throws Exception;//忽略
    public ResultDto<String>  browse(String memberId,ActionInput inputDto)throws Exception;//浏览
    public ResultDto<String>  whetherIsDone(String memberId,ActionInput inputDto)throws Exception;//查询用户是否有操作记录
    public ResultDto<String>  addAction(String memberId,int targetType,int type,String targetId,String information);//添加用户行为记录
    public boolean addCounter(String memberId,int type, ActionInput inputDto);//计数 - 增
    public boolean subCounter(String memberId,int type, ActionInput inputDto);//计数 - 减
}
