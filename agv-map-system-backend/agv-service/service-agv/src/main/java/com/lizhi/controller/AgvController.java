package com.lizhi.controller;

import com.lizhi.astart.service.NoPathFoundException;
import com.lizhi.domain.dto.AgvSnapshotDTO;
import com.lizhi.domain.dto.AgvTaskDTO;
import com.lizhi.domain.po.AgvSnapshot;
import com.lizhi.domain.po.Node;
import com.lizhi.result.Result;
import com.lizhi.service.AgvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agv")
public class AgvController {

    private final AgvService agvService;

    /**
     * 动态加车
     */
    @PostMapping("/batch/{mun}")
    public Result<AgvSnapshot> batchAgv(@PathVariable Integer mun){
        agvService.batchAgv(mun);
        return Result.success();
    }
    /**
     * 单车遥控充电
     */
    @PostMapping("/charge/{agvCode}")
    public Result<AgvSnapshot> chargeAgv(@PathVariable String agvCode) throws NoPathFoundException {
        agvService.chargeAgv(agvCode);
        return Result.success();
    }

    /**
     * 获取单辆小车当前位置
     */
    @GetMapping("/{agvCode}")
    public Result<AgvSnapshot> getAgv(@PathVariable String agvCode){
        AgvSnapshot agv = agvService.getAgv(agvCode);
        return agv == null ? Result.error("未找到小车当前位置") : Result.success(agv);
    }

    /**
     * 获取所有小车当前位置
     * @return
     */
    @PostMapping("/list")
    public Result<List<AgvSnapshot>> getAllAgv() throws NoPathFoundException {
        List<AgvSnapshot> agvList = agvService.getAllAgv();
        return Result.success(agvList);
    }

    /**
     * 记录小车当前位置
     */
    @PostMapping("/report")
    public Result<AgvSnapshot> reportAgv(@RequestBody AgvSnapshotDTO agvSnapshotDTO){
        agvService.reportAgv(agvSnapshotDTO);
        return Result.success();
    }

    /**
     * 开始agv任务
     * @param agvTaskDTO
     * @return
     */
    @PostMapping("/path")
    public Result<List<Node>> startTask(@RequestBody AgvTaskDTO agvTaskDTO) throws NoPathFoundException, InterruptedException {
        List<Node> pathList = agvService.startTask(agvTaskDTO);
        return Result.success(pathList);
    }

    /**
     * 手动停止agv小车运行
     * @return
     */
    @PostMapping("/stop")
    public Result<AgvSnapshot> stopAgv(@RequestBody AgvSnapshotDTO agvSnapshotDTO){
        agvService.stopAgv(agvSnapshotDTO);
        return  Result.success();
    }

    /**
     * 手动启动agv小车运行
     * @return
     */
    @PostMapping("/start")
    public Result<AgvSnapshot> startAgv(@RequestBody AgvSnapshotDTO agvSnapshotDTO){
        agvService.startAgv(agvSnapshotDTO);
        return  Result.success();
    }

    /**
     * 小车速度调节
     * @param agvSnapshotDTO
     * @return
     */
    @PostMapping("/speed")
    public Result<AgvSnapshot> setSpeed(@RequestBody AgvSnapshotDTO agvSnapshotDTO){
        agvService.setSpeed(agvSnapshotDTO);
        return Result.success();
    }

    /**
     * 清除所有任务
     * @return
     */
    @PostMapping("/clear")
    public Result<AgvSnapshot> clearAllTask(){
        agvService.clearAllTask();
        return Result.success();
    }

}
