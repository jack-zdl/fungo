package com.fungo.system;

import com.fungo.system.service.IncentAccountCoinDaoService;
import com.game.common.vo.UserFunVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/9/25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private IncentAccountCoinDaoService incentAccountCoinDaoService;

    @Test
    public void deleteUserFun(){
        UserFunVO userFunVO = new UserFunVO();
        userFunVO.setMemberId("287c0f55aaf7436b89c0a73964efff3c");
        userFunVO.setNumber( 10 );
        userFunVO.setDescription( "删除文章" );
        incentAccountCoinDaoService.deleteUserFun( userFunVO);
    }
}
