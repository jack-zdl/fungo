package com.fungo.community.service.impl;

import com.fungo.community.dao.mapper.CmmCommentDao;
import com.fungo.community.service.ICounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CounterServiceImpl implements ICounterService {

	@Autowired
	private CmmCommentDao actionDao;
	
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
	public boolean subCounter(String tableType, String fieldType, String id) {
		Map<String,String> map =new HashMap<String,String>();
		map.put("tableName", tableType);
		map.put("fieldName", fieldType);
		map.put("id", id);
		map.put("type","sub");
		return actionDao.updateCountor(map);
	}
}
