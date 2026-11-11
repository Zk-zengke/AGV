package com.lizhi.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgvSnapshot implements Serializable {
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private List<Node> plannedPath; // 规划路径
    private int pathIndex;     // 当前路径索引
    private static final long serialVersionUID = 1L;

    private String AgvCode;        // AGV编号，如 AGV001
    private Integer x;        // 坐标
    private Integer y;
    private Integer direction; // 方向角，单位度
    private String status;    // 状态：idle、running、charging、error...
    private double battery;  // 电量百分比
    private Integer speed;    // 当前速度 cm/s
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer taskId;    // 正在执行的任务id，无任务为 null
    private String description;// 正在执行的任务，无任务为 null
    private String lastUpdate; // ISO8601 时间字符串，如 2025-09-19T08:30:00.000Z
    private Integer isCharge; //0未在充电1在充电
    private List<AgvPath> agvPaths;

    // 最终终点，仅内存使用、不持久化，供时间窗每拍重规划时定位最终目标
    @TableField(exist = false)
    private Node finalGoal;
}
