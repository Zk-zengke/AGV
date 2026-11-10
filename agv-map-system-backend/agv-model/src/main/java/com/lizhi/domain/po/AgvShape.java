package com.lizhi.domain.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgvShape implements Serializable {

    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private Integer angle;

}
