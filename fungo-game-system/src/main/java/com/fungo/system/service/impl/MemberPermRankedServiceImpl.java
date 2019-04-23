package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.IncentMbPermRanked;
import com.fungo.system.service.IMemberPermRankedService;
import com.fungo.system.service.IncentMbPermRankedService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.FunGoEHCacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberPermRankedServiceImpl implements IMemberPermRankedService {

	@Autowired
	private IncentMbPermRankedService incentMbPermRankedService;
	
	@Override
//	@Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_MemberPermRanked'")
	public List<IncentMbPermRanked> getIncentMbPermRankedList() {
		List<IncentMbPermRanked> premRankedList = new ArrayList<>();
		String cacheKey = FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_MemberPermRanked";
		
		 Object cacheObj = FunGoEHCacheUtils.get(FunGoGameConsts.CACHE_EH_NAME, cacheKey);
		 if (null != cacheObj) {
			
			 premRankedList = JSONObject.parseArray(String.valueOf(cacheObj), IncentMbPermRanked.class);
             return premRankedList;
         }else {
        	 premRankedList = incentMbPermRankedService.selectList(new EntityWrapper<IncentMbPermRanked>());
        	 PropertyFilter profilter = new PropertyFilter() {

				@Override
				public boolean apply(Object object, String name, Object value) {
					
					if(name.equals("createdAt")) {
						return false;
					}else if(name.equals("updatedAt")) {
						return false;
					}
					// TODO Auto-generated method stub
					return true;
				}
        		 
        	 };
        	 FunGoEHCacheUtils.put(FunGoGameConsts.CACHE_EH_NAME, cacheKey, JSONObject.toJSONString(premRankedList,profilter));
         }
		
		return premRankedList;
	}

}
