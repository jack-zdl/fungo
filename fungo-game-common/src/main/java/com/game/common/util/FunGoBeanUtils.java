package com.game.common.util;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <p>
 * javabean工具类
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class FunGoBeanUtils {


    /**
     * JavaBean转换为Map
     *
     * @param bean
     * @param map
     * @param beanClazz
     * @return
     * @throws Exception
     */
    public static Map<String, Object> bean2map(Object bean, Map<String, Object> map, Class beanClazz) throws Exception {
        if (null == map) {
            map = new HashMap<String, Object>();
        }
        
        //获取指定类（Person）的BeanInfo 对象
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClazz, Object.class);
        //获取所有的属性描述器
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            String key = pd.getName();
            Method getter = pd.getReadMethod();
            Object value = getter.invoke(bean);
            map.put(key, value);
        }
        return map;

    }


    /**
     * Map转换为JavaBean
     *
     * @param map
     * @param clz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T map2bean(Map<String, Object> map, Class<T> clz) throws Exception {
        //创建JavaBean对象
        T obj = clz.newInstance();
        //获取指定类的BeanInfo对象
        BeanInfo beanInfo = Introspector.getBeanInfo(clz, Object.class);
        //获取所有的属性描述器
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            Object value = map.get(pd.getName());
            Method setter = pd.getWriteMethod();
            setter.invoke(obj, value);
        }

        return obj;

    }

}
