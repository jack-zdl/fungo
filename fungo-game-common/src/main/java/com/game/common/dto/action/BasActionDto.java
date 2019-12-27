package com.game.common.dto.action;

import com.game.common.api.InputPageDto;
import lombok.Data;

import java.util.Date;

/**
 *
 * @Author lyc
 * @create 2019/5/10 14:18
 */
@Data
public class BasActionDto extends InputPageDto {
    private String id;
    /**
     * 行为类型
     *
     */
    private Integer type;
    /**
     * 状态
     *  0 正常
     *  -1 删除
     */
    private Integer state;
    /**
     * 业务id
     */
    private String targetId;
    /**
     * 内容
     */
    private String information;
    /**
     * 业务类型
     *
     */
    private Integer targetType;
    /**
     * 会员id
     */
    private String memberId;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 功能描述:
     * @auther: dl.zhang
     * @date: 2019/9/26 16:40
     */
    private boolean liked;
    /**
     * 圈子id
     */
    private String circleId;
}
