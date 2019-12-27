package com.game.common.vo;

import com.game.common.api.InputPageDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/6/21
 */
@Setter
@Getter
public class CircleGamePostVo extends InputPageDto {
    
    /**
     * 功能描述: 传递参数类型 对应枚举类 CircleGamePostTypeEnum
     * @auther: dl.zhang
     * @date: 2019/7/13 14:33
     */
    private int type;

    @Size(max = 32 ,min = 32 ,message = "游戏id不符合规范")
    @NotBlank
    private String gameId;
    private String post;

    public CircleGamePostVo(int type, String gameId, String post) {
        this.type = type;
        this.gameId = gameId;
        this.post = post;
    }

    public CircleGamePostVo() {
    }

    public enum CircleGamePostTypeEnum {

        GAMESID (1,"游戏id"),
        POSTID(2,"文章id");

        int key;
        String value;

        CircleGamePostTypeEnum(int s, String s1) {
            this.key = s;
            this.value = s1;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

}
