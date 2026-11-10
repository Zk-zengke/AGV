package com.lizhi.controller;


import com.lizhi.domain.dto.AgvMapDTO;
import com.lizhi.domain.dto.AgvTaskDTO;
import com.lizhi.domain.po.AgvMap;
import com.lizhi.result.Result;
import com.lizhi.service.AgvMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map")
@Slf4j
public class AgvMapController {

    private final AgvMapService agvMapService;

    public AgvMapController(AgvMapService agvMapService) {
        this.agvMapService = agvMapService;
    }

    /**
     * 保存地图信息
     * @return
     */
    @PostMapping("/save")
    public Result<AgvMap> saveMap(@RequestBody AgvMapDTO agvMapDTO) {
        return agvMapService.saveMap(agvMapDTO);
    }
    /**
     * 获取当前地图信息
     * @return
     */
    @GetMapping("/current")
    public Result<AgvMap> getCurrentMap() {
        AgvMap agvMap = agvMapService.getCurrentMap();
        return Result.success(agvMap);
    }

    /**
     * 重置地图
     * @return
     */
    @PostMapping("/reset")
    public Result<AgvMap> resetMap(){
        AgvMap agvMap = agvMapService.resetMap();
        return Result.success(agvMap);
    }

    /**
     * 获取当前站点
     */
    @GetMapping("/station")
    public Result<List<AgvMap.Station>> getStation(){
        AgvMap agvMap = agvMapService.getStation();
        return Result.success(agvMap.getStations());
    }



}
