package com.lizhi.controller;

import com.lizhi.domain.query.AgvLogQuery;
import com.lizhi.result.PageResult;
import com.lizhi.result.Result;
import com.lizhi.service.AgvLogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class AgvLogController {
    private final AgvLogService agvLogService;

    public AgvLogController(AgvLogService agvLogService) {
        this.agvLogService = agvLogService;
    }

    /**
     * 系统日志分页查询
     * @param agvLogQuery
     * @return
     */
    @PostMapping("/page")
    public Result<PageResult> page(@RequestBody AgvLogQuery agvLogQuery){
        PageResult pageResult = agvLogService.pageQuery(agvLogQuery);
        return Result.success(pageResult);
    }
}
