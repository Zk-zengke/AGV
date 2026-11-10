package com.lizhi.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgvPath {
    private Long pathId;
    private String agvCode;
    private Integer x;
    private Integer y;
    private String type;
    private Integer mapId;
}
