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

    /**
     * 功能描述: 提醒的用户集合
     * @date: 2019/10/23 14:33
     */
    private List<String> commentIds;

    /**
     * 功能描述: 剩余天数
     * @date: 2019/10/23 14:32
     */
    private int days;

    private int type;

    public enum TypeEnum{
        POSTEVALUATE(1,"文章评论"),
        COMMENTEVALUATE (2,"心情评论"),
        POSTREPLY(3,"文章回复"),
        COMMENTREPLY(4,"心情回复"),
        GAMEREPLY(5,"游戏回复"),
        GAMEVIP(11,"游戏VIP"),
        BAIJINVIP(12,"白金VIP")
        ;

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
