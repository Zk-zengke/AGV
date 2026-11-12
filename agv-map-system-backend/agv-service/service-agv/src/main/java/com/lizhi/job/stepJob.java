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
     * 全局巡检：驱动已收敛，每辆车由 AgvSimulator 内部定时任务对内存对象 step，
     * 这里不再读取数据库对象重复 step，只遍历内存车辆做低电量充电巡检。
     */
    @Scheduled(cron = "0/1 * * * * ?")  // 每 1 秒
    public void steploop() throws NoPathFoundException {
        List<AgvSnapshot> agvList = agvSimulator.getAllCars();
        for (AgvSnapshot car : agvList) {
            if (car.getBattery() <= 30 && car.getIsCharge() == 0){
                agvFeignClient.chargeAgv(car.getAgvCode());
            }
        }
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
