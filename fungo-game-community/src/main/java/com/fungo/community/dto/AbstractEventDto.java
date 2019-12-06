package com.fungo.community.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

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

    public AbstractEventDto(Object source) {
        super( source );
    }

    public enum AbstractEventEnum{
        DELETE_POST(1);
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

