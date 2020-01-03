package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@TableName("t_member_circle")
public class MemberCircle extends Model<MemberCircle> {


    @TableId(value="id",type = IdType.UUID)
    private String id;

    @TableField("member_id")
    private String memberId;

    @TableField("type")
    private Integer type;

    @TableField("circle_id")
    private String circleId;

    @TableField("isactive")
    private String isactive;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_at")
    private Date createdAt;

    @TableField("rversion")
    private Integer rversion;

    @TableField("description")
    private String description;

    private static final long serialVersionUID = 1L;

    @Override
    protected Serializable pkVal() {
        return id;
    }
}