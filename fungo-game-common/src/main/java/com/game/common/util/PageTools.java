package com.game.common.util;


import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.dto.FungoPageResultDto;


public class PageTools {

    /**
     * 设置分页的前一页，后一页数据
     * @param fpage
     * @param page
     */
    public static void pageToResultDto(FungoPageResultDto fpage, Page page) {
        int count = page.getTotal();
        int pages = page.getPages();
        int current = page.getCurrent();

        if (current > pages) {

            fpage.setAfter(-1);
            fpage.setBefore(pages);

        } else {

            fpage.setBefore(current == 1 ? -1 : current - 1);

            if (count != 0) {

                //当前页有数据
                if (fpage.getData().size() > 0) {
                    fpage.setAfter(current == pages ? -1 : current + 1);
                } else {
                    fpage.setAfter(-1);
                }

            } else {
                fpage.setAfter(-1);
            }
        }
        fpage.setPages(pages);
        fpage.setCount(count);
    }


    /**
     *  设置分页的前一页，后一页数据
     * @param fpage api返回数据的封装对象
     * @param total 总记录数
     * @param size  每页显示条数
     * @param current 当前页
     */
    public static void pageToResultDto(FungoPageResultDto fpage, int total, int size, int current) {
        int count = total;
        //总页数
        int pages = (total - 1) / size + 1;

        if (current > pages) {

            fpage.setAfter(-1);
            fpage.setBefore(pages);

        } else {

            fpage.setBefore(current == 1 ? -1 : current - 1);

            if (count != 0) {

                //当前页有数据
                if (fpage.getData().size() > 0) {
                    fpage.setAfter(current == pages ? -1 : current + 1);
                } else {
                    fpage.setAfter(-1);
                }

            } else {

                fpage.setAfter(-1);
            }

        }

        fpage.setCount(count);
    }


    //------------
}
