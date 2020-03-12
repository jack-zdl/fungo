package com.fungo.system.function;


import com.fungo.system.config.WebSocketEndpointConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/3/12
 */
@ServerEndpoint(value = "/webSocket", configurator = WebSocketEndpointConfigure.class)
@Service
public class WebSocket {


    private static final Logger logger = LoggerFactory.getLogger( WebSocket.class);

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        logger.info("WebSocket sessionId:[{}] onOpen", session.getId());
    }

    /**
     * 关闭连接
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        final String sessionId = session.getId();
        logger.info("WebSocket sessionId:[{}] onClose", sessionId);

    }

    /**
     * 收到消息
     *
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        doOnMessage(message, session);
    }

    private void doOnMessage(String message, Session session) {

        final String sessionId = session.getId();

        logger.info("sessionId:[{}] message:[{}]", sessionId, message);

        try {
            String result = String.format("sessionId:[%s] message:[%s]", sessionId, message);
            //将结果返回给前端
            session.getBasicRemote().sendText(result);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 发生异常
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        final String sessionId = session.getId();
        logger.info("WebSocket sessionId:[{}] onError", sessionId);
        logger.error(error.getMessage(), error);

    }

}
