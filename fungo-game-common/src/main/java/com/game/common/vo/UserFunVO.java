package com.game.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p> 扣减funVO类</p>
 * @Author: dl.zhang
 * @Date: 2019/9/25
 */
@Setter
@Getter
@ToString
public class UserFunVO {
    /**
     * 功能描述: 用户id
     * @auther: dl.zhang
     * @date: 2019/9/25 10:08
     */
    private String memberId;

    /**
     * 功能描述: fun币扣除原因
     * @auther: dl.zhang
     * @date: 2019/9/25 10:08
     */
    private String description;

    /**
     * 功能描述: 扣除fun币金额
     * @auther: dl.zhang
     * @date: 2019/9/25 10:09
     */
    private int number;

    /**
     * 功能描述: 是否管控台 1 是管控台
     * @date: 2019/10/22
     */
    private int type;
    /**
     * 功能描述:  删除的内容是什么类型  1 文章  2  心情  3 评论 4  心情评论
     * @date: 2019/10/22
     */
    private int contentType;
}
