package com.game.common.buriedpoint.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.*;

@Configuration
public class BuriedPointEventConfig {
    private static Logger logger = LoggerFactory.getLogger(BuriedPointEventConfig.class);

    /**
     *  配置支持异步的事件多播器
     *   配置生效原因:
     *   1. 事件多播器的第一次使用是在reflush().initApplicationEventMulticaster()中使用
     *      根据代码逻辑，若容器中有 SimpleApplicationEventMulticaster 则使用容器中的，否则创建一个默认的非异步的多播器
     *
     *   2. 因此我们自己配置一个带有线程池的多播器即可支持异步，问题是多播器的另外一个参数 BeanFactory 如何配置
     *      答案是不用配置 SimpleApplicationEventMulticaster 实现了 BeanFactoryAware 接口 在容器创建bean时 会自动注入该参数
     *      在 AbstractAutowireCapableBeanFactory.invokeAwareMethods()中注入
     */
    @Bean
    public SimpleApplicationEventMulticaster applicationEventMulticaster(){
        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        // 配置支持异步的事件多播器
         simpleApplicationEventMulticaster.setTaskExecutor(buriedPointThreadPoolExecutor());
        return simpleApplicationEventMulticaster;
    }

    /**
     * 构造一个 埋点事件异步使用的线程池
     *   线程池核心策略: 使用有界队列 当队列满了且线程不足时，使用调用方线程处理
     */
    @Bean
    public ThreadPoolExecutor buriedPointThreadPoolExecutor(){
        return new ThreadPoolExecutor(3, Runtime.getRuntime().availableProcessors(), 60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("BuriedPoint-Thread");
                    t.setUncaughtExceptionHandler((t1, e) -> logger.warn("埋点事件上报执行异常:{}",e.getMessage()));
                    return t;
                },
                (r, executor) -> {
                    if (!executor.isShutdown()) {
                        logger.warn("埋点事件异步线程数量不足 使用调用者线程处理");
                        r.run();
                    }
                });
    }

}
