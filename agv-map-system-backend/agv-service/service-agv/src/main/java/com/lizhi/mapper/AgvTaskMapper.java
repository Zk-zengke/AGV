package com.lizhi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.lizhi.domain.po.AgvTask;
import com.lizhi.domain.query.AgvTaskQuery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgvTaskMapper extends BaseMapper<AgvTask> {
    /**
     * 列表分页查询
     * @param agvTaskQuery
     * @return
     */
    Page<AgvTask> pageQuery(AgvTaskQuery agvTaskQuery);

    /**
     * 通过任务id获取数据
     * @param taskId
     * @return
     */
    AgvTask getById(Integer taskId);

    /**
     * 更新任务
     * @param agvTask
     */
    void updateTask(AgvTask agvTask);
}
