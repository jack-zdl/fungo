package com.game.common.framework.runtime;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 *       获取Bean实例
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContextParam) throws BeansException {
        applicationContext = applicationContextParam;
    }

    public static Object getObject(String id) {
        Object object = null;
        object = applicationContext.getBean(id);
        return object;
    }

    public static <T> T getObject(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static Object getBean(String tClass) {
        return applicationContext.getBean(tClass);
    }

    public <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}
