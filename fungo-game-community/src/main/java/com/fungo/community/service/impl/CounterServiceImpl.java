package com.fungo.community.service.impl;

import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmCommentDao;
import com.fungo.community.service.ICounterService;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.ActionInput;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CounterServiceImpl implements ICounterService {

	@Autowired
	private CmmCommentDao actionDao;
	@Autowired
	private CmmCircleMapper cmmCircleMapper;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 功能描述: todo  增加mq到system微服务通知他用户已关注官方圈
	 * @date: 2019/12/23 17:37
	 */
	@Override
	public boolean addCounter(String tableType, String fieldType, String id) {
		Map<String,String> map =new HashMap<String,String>();
		map.put("tableName", tableType);
		map.put("fieldName", fieldType);
		map.put("id", id);
		map.put("type","add");


		return actionDao.updateCountor(map);
	}

	@Override
	public boolean addCircleCounter(String tableType, String fieldType, String id,String memberId) {
		try {
			Map<String,String> map =new HashMap<String,String>();
			map.put("tableName", tableType);
			map.put("fieldName", fieldType);
			map.put("id", id);
			map.put("type","add");
			int count = cmmCircleMapper.selectOfficialCircle( id);
			if(count > 0){
				AbstractEventDto abstractEventDto = new AbstractEventDto( this );
				abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_CIRCLE.getKey());
				abstractEventDto.setUserId(  memberId);
				abstractEventDto.setObjectId( id);
				applicationEventPublisher.publishEvent(abstractEventDto);
			}
			return actionDao.updateCountor(map);
		}catch (Exception e){

		}
		return false;
	}


	//表字段 增数
	@Override
	public boolean addCounter(String memberId, int type, ActionInput inputDto) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tableName", getTableName(inputDto.getTarget_type()));
		map.put("fieldName", getFieldName(type));
		map.put("id", inputDto.getTarget_id());
		map.put("type", "add");
		return actionDao.updateCountor(map);
	}


	@Override
	public boolean subCounter(String tableType, String fieldType, String id) {
		Map<String,String> map =new HashMap<String,String>();
		map.put("tableName", tableType);
		map.put("fieldName", fieldType);
		map.put("id", id);
		map.put("type","sub");
		return actionDao.updateCountor(map);
	}



	//表字段 减数
	@Override
	public boolean subCounter(String memberId, int type, ActionInput inputDto) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tableName", getTableName(inputDto.getTarget_type()));
		map.put("fieldName", getFieldName(type));
		map.put("id", inputDto.getTarget_id());
		map.put("type", "sub");
		return actionDao.updateCountor(map);
	}


	//根据资源类型获取表名
	private String getTableName(int type) {
		if (type == 0) {
			return "t_member";
		} else if (type == 1) {
			return "t_cmm_post";
		} else if (type == 2) {
			return "t_moo_mood";
		} else if (type == 3) {
			return "t_game";
		} else if (type == 4) {
			return "t_cmm_community";
		} else if (type == 5) {
			return "t_cmm_comment";
		} else if (type == 6) {
			return "t_game_evaluation";
		} else if (type == 7) {
			return "t_reply";
		} else if (type == 8) {
			return "t_moo_message";
		} else if (type == 9) {
			return "t_bas_feedback";
		}
		return null;
	}

	//获取表字段
	private String getFieldName(int type) {
		if (type == 0) {
			return "like_num";
		} else if (type == 4) {
			return "collect_num";
		} else if (type == 5) {
			return "followee_num";
		} else if (type == 12) {
			return "watch_num";
		} else if (type == 15) {
			return "follower_num";
		} else if (type == 7) {
			return "post_num";
		}
		return null;
	}

}
