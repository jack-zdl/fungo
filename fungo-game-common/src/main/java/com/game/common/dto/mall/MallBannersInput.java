package com.game.common.dto.mall;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * <p>
 *  查询点赞数据
 * </p>
 *
 * @author Carlos
 * @since 2019-01-14
 */
@Getter
@Setter
@ToString
public class MallBannersInput implements Serializable {

    /**
     * 关注目标id
     */
    private String target_id;

    //登录用户
    private String login_id;

}
