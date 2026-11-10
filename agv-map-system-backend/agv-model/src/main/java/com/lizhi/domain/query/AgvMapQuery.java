package com.lizhi.domain.query;

import com.lizhi.domain.po.AgvMap;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgvMapQuery extends AgvMap implements Serializable {
    private int page;
    private int pageSize;

}
