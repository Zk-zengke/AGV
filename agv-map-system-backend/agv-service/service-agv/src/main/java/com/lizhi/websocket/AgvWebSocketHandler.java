package com.lizhi.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AGV 状态 WebSocket 处理器：维护所有在线连接，并向其广播车辆状态。
 */
@Slf4j
@Component
public class AgvWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public AgvWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket 连接建立：{}，当前连接数：{}", session.getId(), sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket 连接关闭：{}，状态：{}，当前连接数：{}", session.getId(), status, sessions.size());
    }

    /**
     * 把车辆状态序列化为 JSON 并广播给所有在线连接。
     */
    public void broadcast(Object payload) {
        if (sessions.isEmpty()) {
            return;
        }
        try {
            String json = objectMapper.writeValueAsString(payload);
            TextMessage message = new TextMessage(json);
            for (WebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    continue;
                }
                // 同一 session 的发送加锁，避免多线程并发写导致异常
                synchronized (session) {
                    session.sendMessage(message);
                }
            }
        } catch (Exception e) {
            log.error("WebSocket 广播失败", e);
        }
    }
}
