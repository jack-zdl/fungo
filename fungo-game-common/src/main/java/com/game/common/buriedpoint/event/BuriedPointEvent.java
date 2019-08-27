package com.game.common.buriedpoint.event;

import com.game.common.buriedpoint.model.BuriedPointModel;
import org.springframework.context.ApplicationEvent;

/**
 *  spring中埋点事件
 */
public class BuriedPointEvent extends ApplicationEvent {

    private BuriedPointModel eventModel;

    public BuriedPointEvent(BuriedPointModel eventModel) {
        super(eventModel);
        this.eventModel = eventModel;
    }

    BuriedPointModel getEventModel(){
        return eventModel;
    }




}
