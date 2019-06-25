package com.game.common.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>返回社区下面的文章分类</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/20
 */
@Setter
@Getter
@ToString
public class CirclePostTypeDto {

    /**
     * 功能描述: 社区文章类型
     * @auther: dl.zhang
     * @date: 2019/6/20 15:32
     */
    private String circlePostType;

    /**
     * 功能描述: 社区文章类型名称
     * @auther: dl.zhang
     * @date: 2019/6/20 15:32
     */
    private String circlePostName;


}
