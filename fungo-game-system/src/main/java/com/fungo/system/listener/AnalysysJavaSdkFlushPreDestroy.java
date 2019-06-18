package com.fungo.system.listener;

import com.game.common.util.pc20.analysysjavasdk.AnalysysJavaSdk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class AnalysysJavaSdkFlushPreDestroy {
    @Autowired
    private AnalysysJavaSdk analysysJavaSdk;
 
    @PreDestroy
    public void destory() {
        System.out.println("释放埋点连接..........................");
        analysysJavaSdk.flush();
        analysysJavaSdk.shutdown();
    }
}
