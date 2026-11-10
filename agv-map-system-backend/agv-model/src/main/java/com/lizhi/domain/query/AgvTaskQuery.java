package com.lizhi.domain.query;


import com.lizhi.domain.po.AgvTask;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgvTaskQuery extends AgvTask implements Serializable {

    private int page;
    private int pageSize;

}
