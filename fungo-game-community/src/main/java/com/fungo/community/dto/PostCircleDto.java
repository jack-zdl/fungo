package com.fungo.community.dto;

import com.fungo.community.entity.CmmCircle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>由文章获取的圈子详情带有</p>
 * <li>圈子下文章数目</li>
 * @Date: 2019/12/5
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class PostCircleDto extends CmmCircle {

    private Long postNum;
}
