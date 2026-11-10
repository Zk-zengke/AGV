package com.lizhi.astart.heuristic;


import com.lizhi.domain.po.Node;

/**
 * 启发式函数接口
 */
public interface Heuristic {
    /**
     * 估计当前节点到目标节点的距离
     * @param current 当前节点
     * @param goal 目标节点
     * @return 估计距离
     */
    double estimate(Node current, Node goal);
}
