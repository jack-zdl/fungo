package com.game.common.dto;

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
public class AbstractTaskEventDto extends ApplicationEvent {

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
    /************** end eventType = 4*********************/

    public AbstractTaskEventDto(Object source) {
        super( source );
    }


}

