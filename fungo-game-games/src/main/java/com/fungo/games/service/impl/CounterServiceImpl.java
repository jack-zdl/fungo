package com.fungo.games.service.impl;

import com.fungo.games.dao.GameDao;
import com.fungo.games.service.ICounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CounterServiceImpl implements ICounterService {
	/*@Autowired
	private BasActionDao actionDao;*/

	@Autowired
	private GameDao gameDao;
	
	@Override
	public boolean addCounter(String tableType, String fieldType, String id) {
		Map<String,String> map =new HashMap<String,String>();
		map.put("tableName", tableType);
		map.put("fieldName", fieldType);
		map.put("id", id);
		map.put("type","add");
		return gameDao.updateCountor(map);
	}

	@Override
	public boolean subCounter(String tableType, String fieldType, String id) {
		Map<String,String> map =new HashMap<String,String>();
		map.put("tableName", tableType);
		map.put("fieldName", fieldType);
		map.put("id", id);
		map.put("type","sub");
		return gameDao.updateCountor(map);
	}
}
