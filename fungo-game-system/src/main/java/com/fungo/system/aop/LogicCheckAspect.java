package com.fungo.system.aop;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.*;
import com.fungo.system.entity.IncentRanked;
import com.fungo.system.entity.IncentRuleRank;
import com.fungo.system.entity.IncentRuleRankGroup;
import com.fungo.system.service.*;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.MemberUserProfile;
import com.game.common.enums.CommonEnum;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.util.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>逻辑检查</p>
 * @Date: 2019/12/5
 */
@Order(1)
@Aspect
@Component
public class LogicCheckAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger( LogicCheckAspect.class);

    @Autowired
    private IncentRuleRankService rankRuleService;
    @Autowired
    private IncentRankedService rankedService;
    @Autowired
    private IncentRuleRankGroupDao incentRuleRankGroupDao;


    @Pointcut("execution(* com.fungo.system.controller.UserController.*(..)) && @annotation(com.game.common.util.annotation.LogicCheck)")
    public void before(){
    }

    @Before("before()")
    public void requestLogicCheck(JoinPoint joinPoint) throws Exception {
            LogicCheck limit = this.getAnnotation(joinPoint);
            if(limit == null) {
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String[] loginc = limit.loginc();
            Arrays.stream( loginc ).forEach( s -> {
                if(LogicCheck.LogicEnum.BANNED_AUTH.getKey().equals( s )){
                    List<String> memberIds = new ArrayList<>(  );
                    MemberUserProfile member = (MemberUserProfile)request.getAttribute("member");
                    if(member !=null){
                        memberIds.add( member.getLoginId());
                    }
                    AuthorBean author = new AuthorBean();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        //荣誉,身份图片
                        List<IncentRanked> list = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", member.getLoginId()));
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
                    }
                    if(!(author.getCircleLevel() == 2 )){
                        throw new BusinessException( CommonEnum.UNACCESSRULE);
                    }
                }
            });
    }

    /**
     *
     * @Description: 获得注解
     * @param joinPoint
     * @return
     * @throws Exception
     *
     * @author leechenxiang
     * @date 2016年12月14日 下午9:55:32
     */
    private LogicCheck getAnnotation(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation( LogicCheck.class);
        }
        return null;
    }
}
