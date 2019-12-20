package com.fungo.system.ScoreRuleTest;

import com.fungo.system.entity.Member;
import org.apache.catalina.User;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/20
 */

public class OptionalTest {

    @Test
    public void  test() {
        System.out.println("----------------");
        Member user = new Member();
        user.setId( "111111111111111" );
        Member user2 = new Member();
        user2.setId( "222222222222222222" );
        Member result = Optional.ofNullable(user).orElse(user2);

        assertEquals("111111111111111", result.getId());
    }
}
