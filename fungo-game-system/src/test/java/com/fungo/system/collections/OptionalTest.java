package com.fungo.system.collections;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/18
 */

public class OptionalTest {

    @Test
    public void option(){
        String name = "John";
        Optional<String> opt = Optional.ofNullable(name);
        assertEquals("John", opt.get());
        System.out.println("--------------");
            System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

}
