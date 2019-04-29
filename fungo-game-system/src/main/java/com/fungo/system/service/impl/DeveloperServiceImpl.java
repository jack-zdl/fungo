package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.entity.Developer;
import com.fungo.system.entity.DeveloperGameRel;
import com.fungo.system.proxy.IDeveloperProxyService;
import com.fungo.system.service.DeveloperGameRelService;
import com.fungo.system.service.DeveloperService;
import com.fungo.system.service.IDeveloperService;
import com.game.common.config.MyThreadLocal;
import com.game.common.dto.DeveloperGame.DeveloperGameOut;
import com.game.common.dto.DeveloperGame.DeveloperGamePageInput;
import com.game.common.dto.DeveloperGame.DeveloperQueryIn;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.AddGameInputBean;
import com.game.common.dto.game.GameHistoryOut;
import com.game.common.dto.game.GameOutBean;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/28
 */
@Service
public class DeveloperServiceImpl implements IDeveloperService {

    @Autowired
    private DeveloperGameRelService dgrService;

    @Autowired
    private IDeveloperProxyService iDeveloperProxyService;

    @Override
    public ResultDto<String> addGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
        return null;
    }

    @Override
    public ResultDto<String> updateGame(MemberUserProfile memberUserPrefile, AddGameInputBean input) {
        return null;
    }

    @Override
    public ResultDto<DeveloperGameOut> gameDetail(String gameId, String userId) {
        return null;
    }

    @Override
    public FungoPageResultDto<GameOutBean> gameList(DeveloperGamePageInput input, String userId) {
        //查询游戏开发者关联表
        List<DeveloperGameRel> relList = dgrService
                .selectList(new EntityWrapper<DeveloperGameRel>().eq("member_id",userId));

        if(relList != null && relList.size() > 0) {
            List<String> collect = new ArrayList<>();
//			relList.stream().map(DeveloperGameRel::getGameId).collect(Collectors.toList());
            for(DeveloperGameRel rel:relList) {
                if(!CommonUtil.isNull(rel.getGameId())) {
                    collect.add(rel.getGameId());
                }
            }
            return iDeveloperProxyService.gameList(collect,input.getPage(),input.getLimit());
        }
        return null;
    }

    @Override
    public FungoPageResultDto<GameHistoryOut> gameHistory(String userId, DeveloperGamePageInput input) {
        return null;
    }

    @Override
    public ResultDto<List<Map<String, Object>>> gemeAnalyzeLog(DeveloperQueryIn input) {
        return null;
    }

    @Override
    public ResultDto<List<Map<String, Object>>> messageList() {
        return null;
    }

    @Override
    public ResultDto<Map<String, Integer>> communityAnalyze(DeveloperQueryIn input) throws ParseException {
        return null;
    }

    @Override
    public boolean checkDpPower(String memberId) {
        return false;
    }
}
