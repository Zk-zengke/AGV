package com.lizhi.domain.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgvLog {

    /**
     * 日志ID
     */
    private Integer logId;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    /**
     * 日志级别(info,warning,error)
     */
    private String level;

    /**
     * 日志消息
     */
    private String message;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
