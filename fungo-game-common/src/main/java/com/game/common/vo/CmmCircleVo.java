package com.game.common.vo;

import com.game.common.api.InputPageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@Getter
@Setter
public class CmmCircleVo extends InputPageDto {
    
    /**
     * 功能描述: <p>查询类型</p>
     * <p>
     *     0 全部查询
     *     1 最近浏览的圈子
     *     2 我关注的圈子
     * </p>
     * @auther: dl.zhang
     * @date: 2019/6/11 16:14
     */
    private String sorttype;

    /**
     * 功能描述:关键字查询
     * @auther: dl.zhang
     * @date: 2019/6/11 16:16
     */
    private String keyword;

    private String id;

    private String circleName;

    private String circleIcon;

    private String intro;

    private Integer type;

    private Integer state;

    private Integer memberNum;

    private Integer hotValue;

    private Integer sort;

}
