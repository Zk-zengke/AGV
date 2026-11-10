package com.lizhi.service;

import com.lizhi.domain.po.AgvLog;
import com.lizhi.domain.query.AgvLogQuery;
import com.lizhi.result.PageResult;

public interface AgvLogService{
    /**
     * 系统日志分页查询
     * @param agvLogQuery
     * @return
     */
    PageResult pageQuery(AgvLogQuery agvLogQuery);

}
