package com.fungo.community.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.service.CmmCircleService;
import org.springframework.stereotype.Service;

@Service
public class CmmCircleServiceImap extends ServiceImpl<CmmCircleMapper, CmmCircle> implements CmmCircleService {
}
