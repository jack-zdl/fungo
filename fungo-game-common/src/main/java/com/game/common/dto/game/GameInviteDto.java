package com.game.common.dto.game;

import com.baomidou.mybatisplus.annotations.TableField;
import com.game.common.api.InputPageDto;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/13 18:55
 */
@Data
public class GameInviteDto extends InputPageDto {
    private String id;
    /**
     * 会员id
     */
    private String memberId;
    /**
     * 游戏ID
     */
    private String gameId;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 被邀请人
     */
    private String inviteMemberId;

    private String noticeId;
}
