package com.game.common.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.game.common.entity.BasNotice;

/**
 * <p>
  * 通知消息 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-15
 */
public interface BasNoticeDao extends BaseMapper<BasNotice> {

	//设置消息为已读
	public boolean setIsRead(Map<String,Object> map);
	
	//获取未推送的消息,按照用户分组
	public List<HashMap<String,Object>> getUnpushNotices();
}