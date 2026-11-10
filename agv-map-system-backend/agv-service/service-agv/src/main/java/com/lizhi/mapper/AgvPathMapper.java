package com.lizhi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lizhi.domain.po.AgvPath;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgvPathMapper extends BaseMapper<AgvPath> {
    /**
     * 根据agvCode删除路径
     * @param agvCode
     */
    void deleteByAgvCode(String agvCode);
}
