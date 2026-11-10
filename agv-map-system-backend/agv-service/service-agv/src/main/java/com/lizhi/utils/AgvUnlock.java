package com.lizhi.utils;

import com.lizhi.astart.map.GridMap;
import com.lizhi.astart.service.AStarSolver;
import com.lizhi.astart.service.NoPathFoundException;
import com.lizhi.domain.dto.AgvTaskDTO;
import com.lizhi.domain.po.*;
import com.lizhi.mapper.AgvLogMapper;
import com.lizhi.mapper.AgvMapMapper;
import com.lizhi.mapper.AgvMapper;
import com.lizhi.service.AgvService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AgvUnlock {

    private final AgvMapMapper agvMapMapper;
    private final AgvConversion agvConversion;
    private final AgvLogMapper agvLogMapper;

    public AgvUnlock(AgvMapMapper agvMapMapper, AgvConversion agvConversion, AgvLogMapper agvLogMapper) {
        this.agvMapMapper = agvMapMapper;
        this.agvConversion = agvConversion;
        this.agvLogMapper = agvLogMapper;
    }

    public List<Node> realTimePath(Node startNode, Node endNode, List<AgvPath> agvPaths,String agvCode) throws NoPathFoundException {
        AgvMap currentMap = agvMapMapper.getCurrentMap();
        List<AgvMap.Obstacle> oldObstacles = currentMap.getObstacles();
        List<AgvMap.Obstacle> obstacles = agvConversion.convertObstaclesToAgvObstacleObjects(oldObstacles);
        GridMap gridMap = new GridMap(false,currentMap.getHeight(),currentMap.getWidth());
        for (AgvMap.Obstacle obstacle : obstacles) {
            gridMap.setWeight(obstacle.getX(), obstacle.getY(), -1);
        }
        List<AgvMap.Station> oldOstations = currentMap.getStations();
        List<AgvMap.Station> stations = agvConversion.convertStationsToAgvStationObjects(oldOstations);
        for (AgvMap.Station station : stations) {
            gridMap.setWeight(station.getX(),station.getY(),-1);
        }
        gridMap.setWeight(startNode.getX(),startNode.getY(),1);
        gridMap.setWeight(endNode.getX(),endNode.getY(),1);
        if (agvPaths != null){
            for (AgvPath agvPath : agvPaths) {
                gridMap.setWeight(agvPath.getX(),agvPath.getY(),-1);
            }
        }
        AStarSolver aStarSolver = new AStarSolver(gridMap);
        if (agvPaths != null){
            for (AgvPath agvPath : agvPaths) {
                gridMap.setWeight(agvPath.getX(),agvPath.getY(),1);
            }
        }
        try {
            return aStarSolver.AstarFindPath(startNode,endNode);
        } catch (NoPathFoundException e) {
            AgvLog agvLog = new AgvLog();
            agvLog.setTimestamp(LocalDateTime.now());
            agvLog.setLevel("error");
            agvLog.setMessage(agvCode+" 未找到有效路径，无法到达目标点");
            agvLogMapper.insert(agvLog);
            throw new RuntimeException(e);
        }

    }
}
