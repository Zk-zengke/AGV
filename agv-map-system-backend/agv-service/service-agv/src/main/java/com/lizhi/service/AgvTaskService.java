package com.lizhi.service;

import com.lizhi.domain.query.AgvTaskQuery;
import com.lizhi.result.PageResult;

public interface AgvTaskService {
    /**
     * 任务列表分页查询
     * @param agvTaskQuery
     * @return
     */
    PageResult pageQuery(AgvTaskQuery agvTaskQuery);

    /**
     * 取消agv任务
     * @param taskId
     */
    void cancelTask(Integer taskId);
}
