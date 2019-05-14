package com.game.common.cache;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *      模糊删除缓存
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FunGoCacheRemove {

    /**
     * 缓存名称
     * @return
     */
    String value();

    /**
     * 缓存数据key
     * @return
     */
    String[] key();


}
