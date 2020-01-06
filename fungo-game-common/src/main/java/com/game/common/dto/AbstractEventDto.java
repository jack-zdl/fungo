package com.game.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * <p> 删除文章事件DTO类</p>
 * @Date: 2019/12/5
 */
@Setter
@Getter
@ToString
public class AbstractEventDto extends ApplicationEvent {

    /**
     * 功能描述:
     * <li> 1 删除文章事件 </li>
     * @date: 2019/12/5 14:50
     */
    private int eventType;

    /************* start eventType = 1 **********************/
    private int score;

    private String userId;

    private String postId;
    /************** end eventType = 1*********************/

    /************** start eventType = 2*********************/
//    private String userId;

    /************** end eventType = 2*********************/

    /************** start eventType = 3*********************/
//    private String userId;
    /************** end eventType = 3*********************/

    /************** start eventType = 4*********************/
    private int followType;
//    private String userId;
    private String ObjectId;
    private List<String> ObjectIdList;
    /************** end eventType = 4*********************/

    /************** end eventType = 5*********************/
    private int type;
    private int auth;
    /************** end eventType = 5*********************/

    public AbstractEventDto(Object source) {
        super( source );
    }

    public enum AbstractEventEnum{
        DELETE_POST(1),
        USER_LOGIN(2),
        USER_LOGOUT(3),
        USER_FOLLOW(4),
        USER_UNFOLLOW(5),
        UPDATE_POST(6),
        ADD_LIKE(7),
        DELETE_LIKE(8),
        EDIT_USER(9),
        FOLLOW_OFFICIAL_USER(10),
        BROWSE_SHOP(11),
        BINDQQ_USER(12),
        BINDWECHAT_USER(13),
        BINDWEIBO_USER(14),
        OPENPUSH_USER(15),
        VPN_GAME(16),
        FOLLOW_ONE_OFFICIAL_USER(17),
        FOLLOW_ONE_OFFICIAL_CIRCLE(18),

        TASK_USER_CHECK_OFFICIAL_USER(19),
        TASK_USER_CHECK_OFFICIAL_CIRCLE(20),
        TASK_USER_CHECK(21),

        TASK_USER_CHECK_BIND_QQ(22),
        TASK_USER_CHECK_BIND_WEIBO(23),
        TASK_USER_CHECK_BIND_WECHAT(24),
        TASK_USER_CHECK_BIND_QQ_WEIBO_WECHAT(25),

        EDIT_USER_MESSAGE(26);
        int key;
        AbstractEventEnum(int k){
            this.key = k;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }
    }
}

