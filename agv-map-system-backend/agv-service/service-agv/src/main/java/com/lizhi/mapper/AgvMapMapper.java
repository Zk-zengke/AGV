package com.lizhi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lizhi.domain.po.AgvMap;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lfy
* @description 针对表【account_tbl】的数据库操作Mapper
* @createDate 2025-01-08 18:32:50
* @Entity com.atguigu.account.bean.AccountTbl
*/
@Mapper
public interface AgvMapMapper extends BaseMapper<AgvMap> {

    /**
     * 保存地图信息
     * @param agvMap
     */
    void saveAgv(AgvMap agvMap);
    /**
     * 更新地图信息
     * @param agvMap
     * @return
     */
    void updateMap(AgvMap agvMap);
    /**
     * 获取当前地图信息
     * @return
     */
    AgvMap getCurrentMap();
    /**
     * 根据id获取地图
     */
    AgvMap getMapById(Integer mapId);

    /**
     * 获取当前站点
     * @return
     */
    AgvMap getStation();
}
