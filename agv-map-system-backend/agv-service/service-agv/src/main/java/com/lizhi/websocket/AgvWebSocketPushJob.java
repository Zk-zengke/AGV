package com.lizhi.websocket;

import com.lizhi.domain.po.AgvSnapshot;
import com.lizhi.service.AgvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每秒把全量车辆状态通过 WebSocket 广播，替代前端定时轮询。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AgvWebSocketPushJob {

    private final AgvService agvService;
    private final AgvWebSocketHandler agvWebSocketHandler;

    @Scheduled(fixedRate = 1000)
    public void push() {
        try {
            List<AgvSnapshot> cars = agvService.getAllAgv();
            // 保持与现有 HTTP 接口一致的响应结构，前端可直接复用数据处理逻辑
            Map<String, Object> payload = new HashMap<>();
            payload.put("code", 200);
            payload.put("data", cars);
            agvWebSocketHandler.broadcast(payload);
        } catch (Exception e) {
            log.error("WebSocket 推送车辆数据失败", e);
        }
    }
}
