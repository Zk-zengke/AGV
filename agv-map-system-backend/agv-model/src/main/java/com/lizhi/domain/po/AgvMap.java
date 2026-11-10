package com.lizhi.domain.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AgvMap implements Serializable {
    /**
     * 主键ID
     */
    private Integer mapId;

    /**
     * 地图ID，唯一标识一个地图
     */
    private String mapCode;

    /**
     * 地图名称
     */
    private String mapName;

    /**
     * 地图宽度
     */
    private Integer width;

    /**
     * 地图高度
     */
    private Integer height;

    /**
     * 单元格大小
     */
    private Integer cellSize;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 地图描述
     */
    private String description;
    /**
     * 障碍物
     */
    private List<Obstacle> obstacles;
    /**
     * 站点
     */
    private List<Station> stations;
    /**
     * 路径
     */
    private List<AgvPath> paths;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 现版本=1，旧版本=0
     */
    private Integer isCurrent;
    /**
     * 上一版本id，0表示首版
     */
    private Integer parentId;


    @Data
    public static class Obstacle implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer x;
        private Integer y;
    }

    @Data
    public static class Station implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer x;
        private Integer y;
        private String id;
        private String type;
    }
/*
    @Data
    public static class Path implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer x;
        private Integer y;
        private String id;
        private String type;
    }*/

}
