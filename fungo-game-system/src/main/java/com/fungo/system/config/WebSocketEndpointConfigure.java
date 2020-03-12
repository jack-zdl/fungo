package com.fungo.system.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.websocket.server.ServerEndpointConfig;

/**
 * WebSocketEndpointConfigure
 *
 * @author Val Song
 */
@Configuration
public class WebSocketEndpointConfigure extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static volatile ApplicationContext applicationContext;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketEndpointConfigure.applicationContext = applicationContext;
    }
}