package com.lizhi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.lizhi.domain.dto.AgvLogDTO;
import com.lizhi.domain.po.AgvLog;
import com.lizhi.domain.query.AgvLogQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgvLogMapper extends BaseMapper<AgvLog> {
    /**
     * 系统日志分页查询
     * @param agvLogQuery
     * @return
     */
    Page<AgvLog> pageQuery(AgvLogQuery agvLogQuery);
}
