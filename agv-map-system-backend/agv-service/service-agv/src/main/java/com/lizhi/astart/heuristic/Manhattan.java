package com.lizhi.astart.heuristic;


import com.lizhi.domain.po.Node;

/**
 * 曼哈顿距离
 */
public class Manhattan implements Heuristic{
    /**
     * 曼哈顿距离
     * @param current
     * @param goal
     * @return
     */
    @Override
    public double estimate(Node current, Node goal) {
        return Math.abs(current.getX()-goal.getX()) + Math.abs(current.getY()-goal.getY());
    }
}
