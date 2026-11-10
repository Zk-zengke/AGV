package com.lizhi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lizhi.domain.dto.AgvSnapshotDTO;
import com.lizhi.domain.po.AgvSnapshot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgvMapper extends BaseMapper<AgvSnapshot> {
    /**
     * 更新agv
     * @param agvSnapshot
     */
    void updateAgv(AgvSnapshot agvSnapshot);
    /**
     * 获取agv
     * @param agvCode
     * @return
     */
    AgvSnapshot getAgv(String agvCode);

    /**
     * 获取所有agv
     * @return
     */
    List<AgvSnapshot> getAllAgv();
    /**
     * 插入agv
     * @param agvSnapshot
     */
    void insertAgv(AgvSnapshot agvSnapshot);

    /**
     * 更新速度状态
     * @param agvSnapshot
     */
    void updateSpeed(AgvSnapshot agvSnapshot);
}
