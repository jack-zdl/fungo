package com.game.common.dto.game;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/11 16:30
 */
@Data
public class BasTagDto {
    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 游戏数
     */
    private Integer gameNum;
    /**
     * 标签组id
     */
    private String groupId;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 排序
     */
    private Integer sort;
}
