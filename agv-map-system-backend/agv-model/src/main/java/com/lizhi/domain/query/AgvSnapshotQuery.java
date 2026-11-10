package com.lizhi.domain.query;

import com.lizhi.domain.po.AgvSnapshot;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgvSnapshotQuery extends AgvSnapshot implements Serializable {
    private int page;
    private int pageSize;

}
