package com.fungo.system.ScoreRuleTest;

import com.fungo.system.entity.IncentTasked;
import com.fungo.system.entity.ScoreRule;
import com.fungo.system.service.IScoreRuleService;
import com.game.common.util.UUIDUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.security.krb5.SCDynamicStoreConfig;

import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ScoreRuleTest {

    @Autowired
    private IScoreRuleService scoreRuleServiceImpl;

    @Test
    public void testUserScoreRule(){
//        String memberId = "287c0f55aaf7436b89c0a73964efff3c";
        String memberId = "db670a48ec9c49f187392c23dd81ab61";
        String exitType = "1";
        scoreRuleServiceImpl.achieveScoreRule( memberId,exitType);
    }

    @Test
    public void insertScoreRule(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "加入3个官方圈子" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("加入3个官方圈子");
        scoreRule.setMax(1);
        scoreRule.setScore( 50 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功加入3个官方圈子+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(1710);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 23 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(1);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "1" ); //  
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }

    @Test
    public void insertScoreRule1(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "加入3个官方圈子" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("加入3个官方圈子");
        scoreRule.setMax(1);
        scoreRule.setScore( 4 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功加入3个官方圈子+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(17100);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 11 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(1);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "2" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }

    @Test
    public void insertScoreRule2(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "一键关注官方账号" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("一键关注官方账号");
        scoreRule.setMax(1);
        scoreRule.setScore( 50 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功一键关注官方账号+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(1720);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 23 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(2);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "4" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }

    @Test
    public void insertScoreRule3(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "一键关注官方账号" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("一键关注官方账号");
        scoreRule.setMax(1);
        scoreRule.setScore( 4 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功一键关注官方账号+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(17200);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 11 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(2);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "8" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }


    @Test
    public void insertScoreRule4(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "浏览一次礼包乐园" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("浏览一次礼包乐园");
        scoreRule.setMax(1);
        scoreRule.setScore( 50 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功浏览一次礼包乐园+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(1730);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 23 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(4);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "16" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }


    @Test
    public void insertScoreRule5(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "浏览一次礼包乐园" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("浏览一次礼包乐园");
        scoreRule.setMax(1);
        scoreRule.setScore( 1 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功浏览一次礼包乐园+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(17300);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 11 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(4);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "32" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }

    @Test
    public void insertScoreRule6(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "开启推送通知" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("开启推送通知");
        scoreRule.setMax(1);
        scoreRule.setScore( 60 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功开启推送通知+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(1740);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 23 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(8);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "64" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }

    @Test
    public void insertScoreRule7(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "开启推送通知" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("开启推送通知");
        scoreRule.setMax(1);
        scoreRule.setScore( 10);
        scoreRule.setCode("");
        scoreRule.setIntro( "成功开启推送通知+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(17400);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 11 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(8);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "128" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }
//
    @Test
    public void insertScoreRule8(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "加速器界面成功导入一款游戏" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("加速器界面成功导入一款游戏");
        scoreRule.setMax(1);
        scoreRule.setScore( 50 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功加速器界面成功导入一款游戏+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(1750);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 23 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(9);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "256" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }

    @Test
    public void insertScoreRule9(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setId( UUIDUtils.getUUID() );
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
        scoreRule.setName( "加速器界面成功导入一款游戏" );
        scoreRule.setIsActive(1);
        scoreRule.setExtra( "" );
        scoreRule.setTip("加速器界面成功导入一款游戏");
        scoreRule.setMax(1);
        scoreRule.setScore( 5 );
        scoreRule.setCode("");
        scoreRule.setIntro( "成功加速器界面成功导入一款游戏+50，上限加1次" );
        scoreRule.setCreatedAt( new Date());
        scoreRule.setUpdatedAt( new Date() );
        scoreRule.setCodeIdt(17500);
        scoreRule.setIncomeFreqIntro( "一个账号仅限完成1次" );
        scoreRule.setIncomieFregType( 2 );
        scoreRule.setActionUrl( "" );
        scoreRule.setTaskType( 11 );
        scoreRule.setCreatorId( "053c730393724ba9a1eb634541c40cc1" );
        scoreRule.setCreatorName( "admin" );
        scoreRule.setPlsTaskId( "" );
        scoreRule.setSort(9);
        scoreRule.setExt1( "v2.7" );
        scoreRule.setExt2( "512" );
        scoreRule.setExt3( "" );
        scoreRule.setAuthLevel( -1 );
        scoreRule.insert();
    }

}
