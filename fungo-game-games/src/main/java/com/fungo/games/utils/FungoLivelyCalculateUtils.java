package com.fungo.games.utils;


import org.apache.commons.lang3.RandomUtils;

/**
 * <p>
 *      活跃度计算工具
 *      一、文章浏览数、游戏下载数计算规则
 *        1.游戏真实下载数和文章浏览数在 1-50 之间时，每增加 1 次下载/阅读，则显示数据增加 1 * （10-13 之间的随机数四舍五入取整）
 *        2.游戏真实下载数和文章浏览数在 51-100 之间时，每增加 1 次下载/阅读，则显示数据增加 1 * （4-7 之间的随机数四舍五入取整）
 *        3.游戏真实下载数和文章浏览数在 100-999 之间时，每增加 1 次下载/阅读，则显示数据增加 1 * （2-3 之间的随机数四舍五入取整）
 * </p>
 *
 * @author mxf
 * @since 2019-05-07
 */
public class FungoLivelyCalculateUtils {


    /**
     * 计算游戏下载数和文章浏览数
     * @param currentCount 当前次数
     * @return
     */
    public static Long calcViewAndDownloadCount(long currentCount) {

        long factor = 1L;
        if (1 <= currentCount && currentCount <= 50) {
            //产生 start <= 随机数 < end 的随机数
            factor = Math.round(RandomUtils.nextDouble(10, 13 + 1));

        } else if (51 <= currentCount && currentCount <= 100) {

            factor = Math.round(RandomUtils.nextDouble(4, 7 + 1));

        } else if (100 < currentCount && currentCount <= 999) {

            factor = Math.round(RandomUtils.nextDouble(2, 3 + 1));

        }
        return currentCount + ( 1 * factor);
    }

}
