package com.game.common.config;

import org.springframework.stereotype.Component;

/**
 * <p></p>
 *  当前线程的变量
 * @Author: dl.zhang
 * @Date: 2019/4/29
 */
@Component
public class MyThreadLocal {
    private static ThreadLocal<String> tokenClass = new ThreadLocal<>();

    private static ThreadLocal<String> lingkaSession  = new ThreadLocal<>();

    public static String getToken() {
        return tokenClass.get();
    }

    public static void setToken(String token) {
        tokenClass.set(token);
    }

    public static String getLingkaSession() {
        return lingkaSession.get();
    }

    public static void setLingkaSession(String session) {
        lingkaSession.set( session );
    }
}
