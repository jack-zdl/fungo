package com.game.common.buriedpoint.event;

import com.game.common.buriedpoint.BuriedPointUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  spring 埋点事件监听器
 */
@Component
public class BuriedPointEventListener implements ApplicationListener<BuriedPointEvent> {
    private static Logger logger = LoggerFactory.getLogger(BuriedPointEventListener.class);

    //接受到消息，回调该方法
    @Override
    public void onApplicationEvent( BuriedPointEvent event) {
        logger.info(getCurrentDate()+"  SelfApplicationListener 接受到了一个事件 开始处理"+event);
        BuriedPointUtils.buriedPoint(event.getEventModel());
      /*  EventModel eventModel = event.getEventModel();
        System.out.println("事件名称是:"+eventModel.getEventName());*/
        // 这里睡眠测试 自定义可异步的多播器
     /*   try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        logger.info(getCurrentDate() + "  SelfApplicationListener 接受到了一个事件 结束处理"+event);
    }

    private String getCurrentDate(){
        Date date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
