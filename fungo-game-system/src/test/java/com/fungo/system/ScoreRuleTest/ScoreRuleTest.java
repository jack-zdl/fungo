package com.fungo.system.ScoreRuleTest;

import com.fungo.system.entity.ScoreRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ScoreRuleTest {

    public void insertScoreRule(){
        ScoreRule scoreRule = new ScoreRule();
        scoreRule.setGroupId( "887e055a257345438944c9fd17ab4855");
    }
}
