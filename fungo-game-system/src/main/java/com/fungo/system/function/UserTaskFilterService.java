package com.fungo.system.function;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.IncentRuleRankGroupDao;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.IncentRuleRankGroup;
import com.fungo.system.service.IScoreRuleService;
import com.fungo.system.service.IncentRankedService;
import com.fungo.system.service.IncentRuleRankService;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.dto.AuthorBean;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.terracotta.modules.ehcache.ToolkitInstanceFactoryImpl.LOGGER;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/30
 */
@Service
public class UserTaskFilterService {

    @Autowired
    private IScoreRuleService scoreRuleServiceImpl;
    @Autowired
    private IncentRankedService rankedService;
    @Autowired
    private IncentRuleRankService rankRuleService;
    @Autowired
    private IncentRuleRankGroupDao incentRuleRankGroupDao;


    public void updateUserTask(String memberId){
        scoreRuleServiceImpl.updateExtBygetTasked( memberId,FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE );
        scoreRuleServiceImpl.updateExtBygetTasked( memberId, FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_TASK );
    }


    public AuthorBean getStatusImages(String memberId){
        AuthorBean author = new AuthorBean();
        ObjectMapper mapper = new ObjectMapper();
            try {
                //荣誉,身份图片
                List<IncentRanked> list = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", memberId));
                for (IncentRanked ranked : list) {
                    if (ranked.getRankType() == 1) {
                        String rankIdtIds = ranked.getRankIdtIds();
                        List<HashMap<String,Object>> medalList = mapper.readValue(rankIdtIds, ArrayList.class);
                        author.setHonorNumber( medalList.size());
                        IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
//                author.setLevel(ranked.getCurrentRankId().intValue());
                        String rankImgs = rank.getRankImgs();
                        ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
                        author.setDignityImg((String) urlList.get(0).get("url"));
                    } else if (ranked.getRankType() == 2) {
                        String rankIdtIds = ranked.getRankIdtIds();
                        List<HashMap<String,Object>> list1 = mapper.readValue(rankIdtIds, ArrayList.class);
                        List<List<HashMap<String,Object>>> statusLists = new ArrayList<>(  );
                        int groupLevel = 0;
                        int circleLevel = 0;
                        for (HashMap<String,Object> map : list1){
                            Integer rankId = (Integer) map.get( "1" );
                            IncentRuleRank rank = rankRuleService.selectById(rankId);//最近获得
                            String rankImgs = rank.getRankImgs();
                            ArrayList<HashMap<String, Object>> urlList = null;
                            IncentRuleRankGroup incentRuleRankGroup = incentRuleRankGroupDao.selectById( rank.getRankGroupId());
                            groupLevel =  incentRuleRankGroup.getAuth() > groupLevel ? incentRuleRankGroup.getAuth() : groupLevel;
                            circleLevel =  incentRuleRankGroup.getAuth() == 2 ? incentRuleRankGroup.getAuth() : circleLevel;
                            try {
                                urlList = mapper.readValue(rankImgs, ArrayList.class);
                                urlList.stream().forEach( x ->{
                                    x.put( "auth", incentRuleRankGroup.getAuth());
                                    x.put( "group", incentRuleRankGroup.getId());
                                    x.put( "groupNmae", incentRuleRankGroup.getGroupName());
                                } );
                                statusLists.add( urlList );
                            } catch (IOException e) {
                                LOGGER.error( "對象轉換异常",e );
                            }
                        }
                        author.setGroupLevel(groupLevel);
                        author.setCircleLevel( circleLevel );
                        author.setStatusImgs(statusLists);
                    } else if (ranked.getRankType() == 3) {
                        //找出获得的荣誉合集 (勛章之類的)
                        String rankIdtIds = ranked.getRankIdtIds();
                        ArrayList<HashMap<String, Object>> rankList = mapper.readValue(rankIdtIds, ArrayList.class);
                        Collections.reverse(rankList);
                        List<String> honorImgList = new ArrayList<>();
                        int i = 0;
                        ArrayList<String> groupIdList = new ArrayList<>();
                        //取前三位
                        for (HashMap<String, Object> map : rankList) {
                            IncentRuleRank rank = rankRuleService.selectById(Long.parseLong(map.get("1") + ""));
                            //同一荣誉取等级最高的一个
                            if (rank != null && !groupIdList.contains(rank.getRankGroupId())) {
                                groupIdList.add(rank.getRankGroupId());
                                if (rank.getRankImgs() != null) {
                                    ArrayList<HashMap<String, Object>> urlkList = mapper.readValue(rank.getRankImgs(), ArrayList.class);
                                    honorImgList.add((String) urlkList.get(0).get("url"));
                                }
                                i++;
                                if (i > 2) {
                                    break;
                                }
                            }
                        }
                        author.setHonorImgList(honorImgList);
                    }
                }
            }catch (Exception e){
                LOGGER.error( "逻辑检查失败,类型为BANNED_AUTH" ,e);
                author = null;
            }
        return author;
    }
}
