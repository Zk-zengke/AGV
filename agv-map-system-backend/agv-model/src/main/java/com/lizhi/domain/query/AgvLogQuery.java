package com.lizhi.domain.query;

import com.lizhi.domain.po.AgvLog;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgvLogQuery extends AgvLog implements Serializable {
    private int page;
    private int pageSize;
}
