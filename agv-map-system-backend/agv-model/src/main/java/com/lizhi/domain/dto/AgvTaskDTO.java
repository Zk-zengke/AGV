package com.lizhi.domain.dto;

import com.lizhi.domain.po.AgvTask;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgvTaskDTO extends AgvTask implements Serializable {

    private Point startPoint;
    private Point endPoint;
    private Point currentPoint;

    @Data
    public static class Point {
        private String stationId;
        private String id;
        private Integer x;
        private Integer y;
    }

}
