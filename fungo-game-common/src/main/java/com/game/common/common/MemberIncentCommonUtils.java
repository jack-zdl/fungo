package com.game.common.common;

/**
 * <p>
 * 			用户成长体系基础业务工具类
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public final class MemberIncentCommonUtils {

    /**
     * 经验值与用户等级对应关系
     * @param exp
     * @return
     */
    public static final int getLevel(long exp) {
        int level = 0;
        if (0 < exp && exp <= 10) {
            level = 1;
        } else if (10 < exp && exp <= 50) {
            level = 2;
        } else if (50 < exp && exp <= 100) {
            level = 3;
        } else if (100 < exp && exp <= 200) {
            level = 4;
        } else if (200 < exp && exp <= 400) {
            level = 5;
        } else if (400 < exp && exp <= 600) {
            level = 6;
        } else if (600 < exp && exp <= 1000) {
            level = 7;
        } else if (1000 < exp && exp <= 2000) {
            level = 8;
        } else if (2000 < exp && exp <= 3000) {
            level = 9;
        } else if (3000 < exp && exp <= 5000) {
            level = 10;
        } else if (5000 < exp && exp <= 10000) {
            level = 11;
        } else if (10000 < exp && exp <= 20000) {
            level = 12;
        } else if (exp > 20000) {
            level = 12;
        }

        return level;

    }


}
