package com.lizhi.controller;

import com.lizhi.domain.po.AgvTask;
import com.lizhi.domain.query.AgvTaskQuery;
import com.lizhi.result.PageResult;
import com.lizhi.result.Result;
import com.lizhi.service.AgvTaskService;
import com.lizhi.service.impl.AgvTaskServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class AgvTaskController {

    private final AgvTaskService agvTaskService;

    public AgvTaskController(AgvTaskService agvTaskService) {
        this.agvTaskService = agvTaskService;
    }

    /**
     * 任务列表分页查询
     * @param agvTaskQuery
     * @return
     */
    @PostMapping("/page")
    public Result<PageResult> page(@RequestBody AgvTaskQuery agvTaskQuery){
        PageResult pageResult = agvTaskService.pageQuery(agvTaskQuery);
        return Result.success(pageResult);
    }

    /**
     * 取消agv任务
     * @param taskId
     * @return
     */
    @GetMapping("/cancel/{taskId}")
    public Result<AgvTask> cancelTask(@PathVariable Integer taskId){
        agvTaskService.cancelTask(taskId);
        return Result.success();
    }
}
