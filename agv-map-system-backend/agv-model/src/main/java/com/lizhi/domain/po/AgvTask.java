package com.lizhi.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgvTask {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Integer taskId;

    /** 任务编号（全局唯一） */
    private String taskCode;

    /** 执行该任务的AGV编号（允许空，表示未分配） */
    private String agvCode;

    /** 任务类型：物料运送、充电等 */
    private String taskType;

    /** 任务状态：waiting/processing/paused/completed/failed/cancelled */
    private String status;

    /** 优先级：low/medium/high */
    private String priority;

    /** 起点站点编号 */
    private String source;

    /** 终点站点编号 */
    private String destination;

    /** 任务开始时间 */
    private LocalDateTime startTime;

    /** 预计结束时间 */
    private LocalDateTime estimatedEndTime;

    /** 实际结束时间 */
    private LocalDateTime actualEndTime;

    /** 任务描述 */
    private String description;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
    /** 开始点位X坐标 */
    private Integer startPointX;
    /** 开始点位Y坐标 */
    private Integer startPointY;
    /** 是否经过开始点位*/
    private Integer passMidPoint;
}
