package com.game.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/8/12
 */
@Setter
@Getter
@ToString
public class DelObjectListVO {

    private List<String> commentIds;

    private int type;

    public enum TypeEnum{
        POSTEVALUATE(1,"文章评论"),
        COMMENTEVALUATE (2,"心情评论"),
        POSTREPLY(3,"文章回复"),
        COMMENTREPLY(4,"心情回复"),
        GAMEREPLY(5,"游戏回复");

        int key;
        String value;

        TypeEnum(int s, String s1) {
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
