package com.game.common.dto.game;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 *  标签组
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/15 15:11
 */
@Data
public class BasTagGroupDto {
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标
     */
    private String icon;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
}
