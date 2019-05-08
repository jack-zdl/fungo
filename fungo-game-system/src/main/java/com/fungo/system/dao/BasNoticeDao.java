package com.fungo.system.dao;



import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.BasNotice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通知消息 Mapper 接口
 * </p>
 * @Author lyc
 * @create 2019/5/5 16:26
 */
public interface BasNoticeDao extends BaseMapper<BasNotice> {

	//设置消息为已读
	public boolean setIsRead(Map<String, Object> map);
	
	//获取未推送的消息,按照用户分组
	public List<HashMap<String,Object>> getUnpushNotices();
}