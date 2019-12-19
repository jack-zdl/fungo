package com.fungo.community.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fungo.community.entity.CmmPost;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/17
 */
@Setter
@Getter
@ToString
public class PostAndCircleDto extends CmmPost {

    private String id;

}
