package com.lizhi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lizhi.domain.po.AgvLog;
import com.lizhi.domain.po.AgvTask;
import com.lizhi.domain.query.AgvLogQuery;
import com.lizhi.mapper.AgvLogMapper;
import com.lizhi.result.PageResult;
import com.lizhi.service.AgvLogService;
import org.springframework.stereotype.Service;

@Service
public class AgvLogServiceImpl implements AgvLogService {
    private final AgvLogMapper agvLogMapper;

    public AgvLogServiceImpl(AgvLogMapper agvLogMapper) {
        this.agvLogMapper = agvLogMapper;
    }

    /**
     * 系统日志分页查询
     * @param agvLogQuery
     * @return
     */
    @Override
    public PageResult pageQuery(AgvLogQuery agvLogQuery) {
        PageHelper.startPage(agvLogQuery.getPage(),agvLogQuery.getPageSize());
        Page<AgvLog> page = agvLogMapper.pageQuery(agvLogQuery);
        return new PageResult(page.getTotal(),page.getResult());
    }
}
