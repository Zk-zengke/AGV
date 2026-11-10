package com.lizhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lizhi.domain.po.AgvSnapshot;
import com.lizhi.domain.po.AgvTask;
import com.lizhi.domain.query.AgvTaskQuery;
import com.lizhi.mapper.AgvMapper;
import com.lizhi.mapper.AgvTaskMapper;
import com.lizhi.result.PageResult;
import com.lizhi.service.AgvTaskService;
import org.springframework.stereotype.Service;


@Service
public class AgvTaskServiceImpl implements AgvTaskService {

    private final AgvTaskMapper agvTaskMapper;
    private final AgvMapper agvMapper;

    public AgvTaskServiceImpl(AgvTaskMapper agvTaskMapper, AgvMapper agvMapper) {
        this.agvTaskMapper = agvTaskMapper;
        this.agvMapper = agvMapper;
    }

    /**
     * 任务列表分页查询
     * @param agvTaskQuery
     * @return
     */
    @Override
    public PageResult pageQuery(AgvTaskQuery agvTaskQuery) {
        PageHelper.startPage(agvTaskQuery.getPage(),agvTaskQuery.getPageSize());
        Page<AgvTask> page = agvTaskMapper.pageQuery(agvTaskQuery);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 取消agv任务
     * @param taskId
     */
    @Override
    public void cancelTask(Integer taskId) {
        AgvTask agvTask = agvTaskMapper.getById(taskId);
        AgvSnapshot agv = agvMapper.getAgv(agvTask.getAgvCode());
        agvTask.setStatus("cancelled");
        agv.setStatus("idle");
        agvMapper.updateAgv(agv);
        agvTaskMapper.updateTask(agvTask);
    }
}
