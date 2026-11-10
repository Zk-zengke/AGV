package com.lizhi.astart.heuristic;


import com.lizhi.domain.po.Node;

/**
 * 欧几里得距离启发式
 */
public class Euclidean implements Heuristic{
    /**
     * 欧几里得距离
     * @param current
     * @param goal
     * @return
     */
    public double estimate(Node current, Node goal)
    {
        int dx = current.getX() - goal.getX();
        int dy = current.getY() - goal.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
