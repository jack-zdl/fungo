package com.game.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

/**
*  t_cmm_operation_log
* @author ysx 2020-01-03
*/
@Setter
@Getter
@ToString
public class CmmOperationLogDto  {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    private String id;

    /**
    * 圈子id
    */
    private String circleId;

    /**
    * 圈主id
    */
    private String memberId;

    /**
    * 操作目标id
    */
    private String targetId;

    /**
    * 目标类型 1.文章 2.用户
    */
    private Integer targetType;

    /**
    * 目标操作前状态
    */
    private String oldTargetState;

    /**
    * 目标操作后状态
    */
    private String newTargetState;

    /**
    * 1.加精 2.置顶 3.修改分类 4.隐藏 5.关闭评论 6.禁言 各状态数字*100 为对应的解除操作 例 100 解除加精
    */
    private Integer actionType;

    /**
    * created_at
    */
    private Date createdAt;

    /**
    * updated_at
    */
    private Date updatedAt;




}