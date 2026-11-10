package com.lizhi.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lizhi.domain.po.AgvSnapshot;
import com.lizhi.domain.po.Node;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.annotation.FieldStrategy;

@Data
public class AgvSnapshotDTO extends AgvSnapshot implements Serializable {

}
