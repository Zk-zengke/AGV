package com.lizhi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lizhi.astart.map.GridMap;
import com.lizhi.astart.service.AStarSolver;
import com.lizhi.astart.service.NoPathFoundException;
import com.lizhi.domain.dto.AgvSnapshotDTO;
import com.lizhi.domain.dto.AgvTaskDTO;
import com.lizhi.domain.po.*;
import com.lizhi.feign.AgvFeignClient;
import com.lizhi.mapper.*;
import com.lizhi.service.AgvService;
import com.lizhi.service.AgvTaskService;
import com.lizhi.utils.AgvConversion;
import com.lizhi.utils.AgvSimulator;
import com.lizhi.utils.AgvUnlock;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.IntStream;


@Service
@Slf4j
public class AgvServiceImpl implements AgvService {

    private final AgvMapMapper agvMapMapper;
    private final AgvSimulator agvSimulator;
    private final AgvMapper agvMapper;
    private final AgvTaskMapper agvTaskMapper;
    private final AgvConversion agvConversion;
    private final AgvTaskService agvTaskService;
    private final AgvUnlock agvUnlock;
    private final AgvPathMapper agvPathMapper;
    private final AgvLogMapper agvLogMapper;


    public AgvServiceImpl(AgvMapMapper agvMapMapper, AgvSimulator agvSimulator, AgvMapper agvMapper, AgvTaskMapper agvTaskMapper, DiscoveryClient discoveryClient, AgvFeignClient agvFeignClient, AgvConversion agvConversion, AgvTaskServiceImpl agvTaskServiceImpl, AgvTaskService agvTaskService, AgvUnlock agvUnlock, AgvPathMapper agvPathMapper, AgvLogMapper agvLogMapper) {
        this.agvMapMapper = agvMapMapper;
        this.agvSimulator = agvSimulator;
        this.agvMapper = agvMapper;
        this.agvTaskMapper = agvTaskMapper;
        this.agvConversion = agvConversion;
        this.agvTaskService = agvTaskService;
        this.agvUnlock = agvUnlock;
        this.agvPathMapper = agvPathMapper;
        this.agvLogMapper = agvLogMapper;
    }

    /**
     * 进行agv任务
     * @param agvTaskDTO
     * @return
     * @throws NoPathFoundException
     */
    @Override
    public List<Node> startTask(AgvTaskDTO agvTaskDTO) throws NoPathFoundException{
        AgvTask agvTask = new AgvTask();
        String agvCode = agvTaskDTO.getAgvCode();
        agvTask.setTaskCode(agvTaskDTO.getTaskCode());
        agvTask.setAgvCode(agvCode);
        agvTask.setTaskType("物料运输");
        agvTask.setStatus("processing");
        agvTask.setPriority("low");
        agvTask.setSource(agvTaskDTO.getStartPoint().getId());
        agvTask.setDestination(agvTaskDTO.getEndPoint().getId());
        agvTask.setStartTime(LocalDateTime.now());
        agvTask.setStartPointX(agvTaskDTO.getStartPoint().getX());
        agvTask.setStartPointY(agvTaskDTO.getStartPoint().getY());
        agvTask.setPassMidPoint(0);
        // 获取仿真器内存车辆对象
        AgvSnapshot agv = agvSimulator.getCar(agvCode);
        if (agv == null) {
            throw new NoPathFoundException("未找到AGV小车: " + agvCode);
        }
        agvTask.setDescription("正在从"+agvTaskDTO.getStartPoint().getId()+"前往"+agvTaskDTO.getEndPoint().getId());
        agv.setDescription(agvTask.getDescription());
        AgvTaskDTO.Point startPoint = agvTaskDTO.getStartPoint();
        AgvTaskDTO.Point currentPoint = agvTaskDTO.getCurrentPoint();
        AgvTaskDTO.Point endPoint = agvTaskDTO.getEndPoint();
        ArrayList<Node> pathList = new ArrayList<>();
        Node currentNode = new Node(currentPoint.getX(), currentPoint.getY());
        Node startNode = new Node(startPoint.getX(), startPoint.getY());
        Node endNode = new Node(endPoint.getX(), endPoint.getY());
        //判断当前点位是否为开始站点
        if(currentPoint != startPoint){
            agvTask.setPassMidPoint(1);
            List<Node> startPath = agvUnlock.realTimePath(currentNode, startNode,null,agvCode);
            List<Node> endPath = agvUnlock.realTimePath(startNode, endNode,null,agvCode);
            // 合并路径
            startPath.addAll(endPath);
            pathList.addAll(startPath);
        }else {
            //从开始站点到达结束站点
            List<Node> startPath = agvUnlock.realTimePath(startNode, endNode,null,agvCode);
            pathList.addAll(startPath);
        }
        agv.setPlannedPath(pathList);
        agv.setPathIndex(0);
        agv.setStatus("running");
        agv.setSpeed(150);
        agvTaskMapper.insert(agvTask);
        agv.setTaskId(agvTask.getTaskId());
        agvMapper.updateAgv(agv);
        AgvLog agvLog = new AgvLog();
        agvLog.setTimestamp(LocalDateTime.now());
        agvLog.setLevel("info");
        agvLog.setMessage(agvCode+" 开始执行任务 " + agv.getTaskId());
        agvLogMapper.insert(agvLog);
        return pathList;
    }

    /**
     * 获取所有小车状态
     * @return
     */
    @Override
    public List<AgvSnapshot> getAllAgv() throws NoPathFoundException {
        List<AgvSnapshot> agvList = agvMapper.getAllAgv();
        for (AgvSnapshot agv : agvList) {
            String agvCode = agv.getAgvCode();
            LambdaQueryWrapper<AgvPath> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AgvPath::getAgvCode,agvCode);
            List<AgvPath> agvPaths = agvPathMapper.selectList(lambdaQueryWrapper);
            agv.setAgvPaths(agvPaths);
        }
        return agvList;
    }

    /**
     * 获取agv小车
     * @param agvCode
     * @return
     */
    @Override
    public AgvSnapshot getAgv(String agvCode) {
        AgvSnapshot agv = agvMapper.getAgv(agvCode);
        LambdaQueryWrapper<AgvPath> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgvPath::getAgvCode,agvCode);
        List<AgvPath> agvPaths = agvPathMapper.selectList(lambdaQueryWrapper);
        agv.setAgvPaths(agvPaths);
        return agv;
    }

    /**
     * 记录小车当前位置
     * @param agvSnapshotDTO
     */
    @Override
    public void reportAgv(AgvSnapshotDTO agvSnapshotDTO) {
        agvSnapshotDTO.setLastUpdate(Instant.now().toString());
        agvMapper.updateAgv(agvSnapshotDTO);
    }

    /**
     * 动态加车
     * @param mun
     */
    @Override
    public void batchAgv(Integer mun) {
        IntStream.rangeClosed(1,mun)
                .mapToObj(i->"AGVplus" + String.format("%04d",i))
                .forEach(agvSimulator::createAndStart);
    }

    /**
     * 遥控小车充电
     * @param agvCode
     */
    public void chargeAgv(String agvCode) throws NoPathFoundException {
        //导航到最近的充电点位
        AgvMap currentMap = agvMapMapper.getCurrentMap();
        AgvSnapshot agv = agvSimulator.getCar(agvCode);
        Integer taskId = agv.getTaskId();
        if (taskId != null){
            agvTaskService.cancelTask(agv.getTaskId());
        }
        Node startNode = new Node(agv.getX(), agv.getY());
        AgvTask agvTask = new AgvTask();
        agvTask.setTaskCode("T" + System.currentTimeMillis() + agv.getAgvCode());
        agvTask.setAgvCode(agvCode);
        agvTask.setTaskType("充电");
        agvTask.setStatus("processing");
        agvTask.setPassMidPoint(0);
        agvTask.setStartPointX(agv.getX());
        agvTask.setStartPointY(agv.getY());
        if(agv.getBattery() <= 30){
            agvTask.setPriority("high");
        }else {
            agvTask.setPriority("low");
        }

        agvTask.setStartTime(LocalDateTime.now());
        // 修复：处理 LinkedHashMap 转换问题
        List<AgvMap.Station> oldStations = currentMap.getStations();
        List<AgvMap.Station> stations = agvConversion.convertStationsToAgvStationObjects(oldStations);
        ArrayList<List<Node>> pathList = new ArrayList<>();
        String stationId = null;
        for (AgvMap.Station station : stations) {
            if ("charge".equals(station.getType())) {  // 修复：使用 equals 比较字符串
                Node charge = new Node(station.getX(), station.getY());
                stationId = station.getId();
                pathList.add(agvUnlock.realTimePath(startNode,charge,null,agvCode));
            }
        }
        agvTask.setDestination(stationId);
        agvTask.setDescription(agvCode + "前往" + stationId + "充电");


        List<Node> shortestPath = pathList.stream()
                .min(Comparator.comparingInt(List::size))
                .orElse(null); // 如果列表为空则返回null

        agvTaskMapper.insert(agvTask);
        agv.setPlannedPath(shortestPath);
        agv.setPathIndex(0);
        agv.setStatus("running");
        agv.setIsCharge(1);
        agv.setDescription(agvTask.getDescription());
        agv.setTaskId(agvTask.getTaskId());
        AgvLog agvLog = new AgvLog();
        agvLog.setTimestamp(LocalDateTime.now());
        agvLog.setLevel("warning");
        agvLog.setMessage(agvCode + " 电量低，自动前往充电站");
        agvLogMapper.insert(agvLog);
        // 到达充电点位后
        agvMapper.updateAgv(agv);
    }

    /**
     * 停止小车运行
     * @param agvSnapshotDTO
     */
    @Override
    public void stopAgv(AgvSnapshotDTO agvSnapshotDTO) {
        AgvSnapshot agvSnapshot = new AgvSnapshot();
        BeanUtils.copyProperties(agvSnapshotDTO,agvSnapshot);
        AgvSnapshot agv = agvSimulator.getCar(agvSnapshot.getAgvCode());
        AgvTask agvTask = agvTaskMapper.getById(agv.getTaskId());
        agvTask.setStatus("paused");
        agv.setStatus("stopping");
        agvTaskMapper.updateTask(agvTask);
    }

    /**
     * 启动agv小车运动
     * @param agvSnapshotDTO
     */
    @Override
    public void startAgv(AgvSnapshotDTO agvSnapshotDTO) {
        AgvSnapshot agvSnapshot = new AgvSnapshot();
        BeanUtils.copyProperties(agvSnapshotDTO,agvSnapshot);
        AgvSnapshot agv = agvSimulator.getCar(agvSnapshot.getAgvCode());
        AgvTask agvTask = agvTaskMapper.getById(agv.getTaskId());
        agv.setStatus("running");
        agvTask.setStatus("processing");
        agv.setSpeed(150);
        agvTaskMapper.updateTask(agvTask);
    }

    /**
     * 设置agv小车速度
     * @param agvSnapshotDTO
     */
    @Override
    public void setSpeed(AgvSnapshotDTO agvSnapshotDTO) {
        String agvCode = agvSnapshotDTO.getAgvCode();
        Integer speed = agvSnapshotDTO.getSpeed();
        AgvSnapshot agv = agvSimulator.getCar(agvCode);
        if (agv != null) {
            agv.setSpeed(speed);
        }
    }

    /**
     * 清除所有任务
     */
    @Override
    public void clearAllTask() {
        List<AgvSnapshot> allAgv = agvMapper.getAllAgv();
        for (AgvSnapshot agvSnapshot : allAgv) {
            AgvSnapshot car = agvSimulator.getCar(agvSnapshot.getAgvCode());
            if (car != null) {
                if (car.getTaskId() != null) {
                    AgvTask agvTask = agvTaskMapper.getById(car.getTaskId());
                    if (agvTask != null) {
                        agvTask.setStatus("cancelled");
                        agvTaskMapper.updateTask(agvTask);
                    }
                }
                car.setStatus("idle");
                car.setTaskId(null);
                car.setPlannedPath(null);
            }
        }
    }
}
