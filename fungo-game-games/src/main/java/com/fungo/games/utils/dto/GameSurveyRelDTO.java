package com.fungo.games.utils.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/29
 */
@Setter
@Getter
@ToString
public class GameSurveyRelDTO {

    @TableId(value = "id",type = IdType.UUID)
    private String id;
    /**
     * 会员id
     */
    @TableField("member_id")
    private String memberId;
    /**
     * 游戏id
     */
    @TableField("game_id")
    private String gameId;
    /**
     * 是否同意协议，0 ，1已同意
     */
    private Integer agree;
    /**
     * 通知状态 0没通知，1已通知
     */
    private Integer notice;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 批次号，打批
     */
    @TableField("batch_no")
    private String batchNo;
    /**
     * 手机型号
     */
    @TableField("phone_model")
    private String phoneModel;
    /**
     * appleId
     */
    @TableField("apple_id")
    private String appleId;
    /**
     * 姓名
     */
    private String surname;
    /**
     * 名称
     */
    private String name;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;

    private String gameName;
}
