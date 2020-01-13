package com.game.common.dto.user;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/1/3
 */
@Setter
@Getter
@ToString
public class MemberNameDTO {

    private String id;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 头像
     */
    private String avatar;

    @ApiModelProperty(value="用户官方身份(2.4.3)",example="")
    private List<List<HashMap<String,Object>>> statusImgs = new ArrayList<>();
}
