package com.lizhi.job;

import com.lizhi.astart.service.NoPathFoundException;
import com.lizhi.domain.dto.AgvSnapshotDTO;
import com.lizhi.domain.po.AgvLog;
import com.lizhi.domain.po.AgvShape;
import com.lizhi.domain.po.AgvSnapshot;
import com.lizhi.feign.AgvFeignClient;
import com.lizhi.mapper.AgvLogMapper;
import com.lizhi.mapper.AgvMapper;
import com.lizhi.service.AgvService;
import com.lizhi.utils.AgvCollisionAvoidance;
import com.lizhi.utils.AgvSimulator;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class stepJob {

    private final AgvMapper agvMapper;
    private final AgvSimulator agvSimulator;
    private final AgvFeignClient agvFeignClient;
    private final AgvLogMapper agvLogMapper;
    private final AgvCollisionAvoidance agvCollisionAvoidance;
    private final AgvService agvService;

    public stepJob(AgvMapper agvMapper, AgvSimulator agvSimulator, AgvFeignClient agvFeignClient, AgvLogMapper agvLogMapper, AgvCollisionAvoidance agvCollisionAvoidance, AgvService agvService) {
        this.agvMapper = agvMapper;
        this.agvSimulator = agvSimulator;
        this.agvFeignClient = agvFeignClient;
        this.agvLogMapper = agvLogMapper;
        this.agvCollisionAvoidance = agvCollisionAvoidance;
        this.agvService = agvService;
    }

    private int dec(int index,int length){
        if (index + 1 == length){
            return 0;
        }
        return index + 1;
    }

    private AgvShape saveAgvShap(AgvSnapshot agv){
        AgvShape agvShape = new AgvShape();
        agvShape.setX(agv.getX());
        agvShape.setY(agv.getY());
        agvShape.setHeight(1);
        agvShape.setWidth(1);
        agvShape.setAngle(agv.getDirection());
        return agvShape;
    }

    /**
     * 更新小车当前状态
     * @throws NoPathFoundException
     */
    @Scheduled(cron = "0/1 * * * * ?")  // 每 1 秒
    public void steploop() throws NoPathFoundException {
        List<AgvSnapshot> agvList = agvMapper.getAllAgv();
        for (AgvSnapshot agvSnapshot : agvList) {
            agvSimulator.step(agvSnapshot);
            if (agvSnapshot.getBattery() <= 30 && agvSnapshot.getIsCharge() == 0){
                agvFeignClient.chargeAgv(agvSnapshot.getAgvCode());
            }
        }
        /*for (int i = 0; i < agvList.size();i++){
            AgvShape agvShape1 = saveAgvShap(agvList.get(i));
            for (int j = 0; j < agvList.size();j++){
                boolean collides = false;
                if (i != j){
                   AgvShape agvShape2 = saveAgvShap(agvList.get(j));
                   collides = agvCollisionAvoidance.collidesWith(agvShape1, agvShape2);
                }
                if (collides && agvList.get(i).getStatus().equals("running")){
                    AgvSnapshotDTO dto = new AgvSnapshotDTO();
                    dto.setAgvCode(agvList.get(i).getAgvCode());
                    agvService.stopAgv(dto);
                    break;
                }
           }

        }*/
    }

    /**
     * 检查当前小车的在线数量
     */
    @Scheduled(cron = "0 0 17 * * ?")
    public void agvNumLog(){
        List<AgvSnapshot> agvList = agvMapper.getAllAgv();
        int size = agvList.size();
        AgvLog agvLog = new AgvLog();
        agvLog.setTimestamp(LocalDateTime.now());
        agvLog.setLevel("info");
        agvLog.setMessage("系统启动完成，当前在线AGV: "+size+"辆");
        agvLogMapper.insert(agvLog);
    }
}
