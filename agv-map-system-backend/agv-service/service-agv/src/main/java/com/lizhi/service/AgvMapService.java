package com.lizhi.service;


import com.lizhi.domain.dto.AgvMapDTO;
import com.lizhi.domain.po.AgvMap;
import com.lizhi.result.Result;

public interface AgvMapService {

    /**
     * 保存地图信息
     * @param agvMapDTO
     */
    Result<AgvMap> saveMap(AgvMapDTO agvMapDTO);
    /**
     * 获取当前地图信息
     * @return
     */
    AgvMap getCurrentMap();

    /**
     * 重置地图
     *
     * @return
     */
    AgvMap resetMap();

    /**
     * 获取当前站点位置
     * @return
     */
    AgvMap getStation();
}
