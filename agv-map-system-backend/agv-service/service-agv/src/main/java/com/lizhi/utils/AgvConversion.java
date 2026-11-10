package com.lizhi.utils;

import com.lizhi.domain.po.AgvMap;
import com.lizhi.domain.po.AgvPath;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class AgvConversion {
    /**
     * 将 Obstacle 字段中的 LinkedHashMap 对象转换为 AgvMap.Obstacle 对象
     * @param rawObstacles 原始障碍物列表，可能包含 LinkedHashMap 或 AgvMap.Obstacle 对象
     * @return 转换后的 AgvMap.Obstacle 对象列表
     */
    public List<AgvMap.Obstacle> convertObstaclesToAgvObstacleObjects(List<?> rawObstacles) {
        if (rawObstacles == null || rawObstacles.isEmpty()) {
            return new ArrayList<>();
        }

        List<AgvMap.Obstacle> result = new ArrayList<>();

        for (Object obj : rawObstacles) {
            if (obj instanceof AgvMap.Obstacle) {
                // 如果已经是 AgvMap.Obstacle 类型，直接添加
                result.add((AgvMap.Obstacle) obj);
            } else if (obj instanceof LinkedHashMap) {
                // 如果是 LinkedHashMap 类型，进行转换
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
                AgvMap.Obstacle obstacle = new AgvMap.Obstacle();

                // 安全地设置各个字段
                if (map.containsKey("x") && map.get("x") != null) {
                    obstacle.setX(((Number) map.get("x")).intValue());
                }
                if (map.containsKey("y") && map.get("y") != null) {
                    obstacle.setY(((Number) map.get("y")).intValue());
                }

                result.add(obstacle);
            }
        }

        return result;
    }

    /**
     * 将 stations 字段中的 LinkedHashMap 对象转换为 AgvMap.Station 对象
     * @param rawStations 原始站点列表，可能包含 LinkedHashMap 或 AgvMap.Station 对象
     * @return 转换后的 AgvMap.Station 对象列表
     */
    public List<AgvMap.Station> convertStationsToAgvStationObjects(List<?> rawStations) {
        if (rawStations == null || rawStations.isEmpty()) {
            return new ArrayList<>();
        }

        List<AgvMap.Station> result = new ArrayList<>();

        for (Object obj : rawStations) {
            if (obj instanceof AgvMap.Station) {
                // 如果已经是 AgvMap.Station 类型，直接添加
                result.add((AgvMap.Station) obj);
            } else if (obj instanceof LinkedHashMap) {
                // 如果是 LinkedHashMap 类型，进行转换
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) obj;
                AgvMap.Station station = new AgvMap.Station();

                // 安全地设置各个字段
                if (map.containsKey("x") && map.get("x") != null) {
                    station.setX(((Number) map.get("x")).intValue());
                }
                if (map.containsKey("y") && map.get("y") != null) {
                    station.setY(((Number) map.get("y")).intValue());
                }
                if (map.containsKey("type") && map.get("type") != null) {
                    station.setType((String) map.get("type"));
                }
                if (map.containsKey("id") && map.get("id") != null) {
                    station.setId((String) map.get("id"));
                }

                result.add(station);
            }
        }

        return result;
    }



}
