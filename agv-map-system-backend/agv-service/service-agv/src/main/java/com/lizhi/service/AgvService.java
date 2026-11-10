package com.lizhi.service;

import com.lizhi.astart.service.NoPathFoundException;
import com.lizhi.domain.dto.AgvSnapshotDTO;
import com.lizhi.domain.dto.AgvTaskDTO;
import com.lizhi.domain.po.AgvSnapshot;
import com.lizhi.domain.po.Node;

import java.util.List;

public interface AgvService {
    /**
     * 开始执行任务
     * @param agvTaskDTO
     */
    List<Node> startTask(AgvTaskDTO agvTaskDTO) throws NoPathFoundException, InterruptedException;

    /**
     * 获取所有agv小车状态
     * @return
     */
    List<AgvSnapshot> getAllAgv() throws NoPathFoundException;

    /**
     * 获取agv小车
     * @param agvCode
     * @return
     */
    AgvSnapshot getAgv(String agvCode);

    /**
     * 记录小车当前位置
     * @param agvSnapshotDTO
     */
    void reportAgv(AgvSnapshotDTO agvSnapshotDTO);

    /**
     * 动态加车
     * @param mun
     */
    void batchAgv(Integer mun);

    /**
     * 遥控小车充电
     * @param agvCode
     */
    void chargeAgv(String agvCode) throws NoPathFoundException;

    /**
     * 停止小车运行
     * @param agvSnapshotDTO
     */
    void stopAgv(AgvSnapshotDTO agvSnapshotDTO);

    /**
     * 启动agv小车运动
     * @param agvSnapshotDTO
     */
    void startAgv(AgvSnapshotDTO agvSnapshotDTO);

    /**
     * 设置agv小车速度
     * @param agvSnapshotDTO
     */
    void setSpeed(AgvSnapshotDTO agvSnapshotDTO);

    /**
     * 清除所有任务
     */
    void clearAllTask();
}
