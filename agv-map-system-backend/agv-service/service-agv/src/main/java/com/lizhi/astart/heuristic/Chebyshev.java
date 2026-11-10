package com.lizhi.astart.heuristic;


import com.lizhi.domain.po.Node;

public class Chebyshev implements Heuristic{
    /**
     *  切比雪夫距离启发式
     * @param current
     * @param goal
     * @return
     */
    public double estimate(Node current, Node goal)
    {
        int dx = Math.abs(current.getX() - goal.getX());
        int dy = Math.abs(current.getY() - goal.getY());
        return Math.max(dx, dy);
    }

}
