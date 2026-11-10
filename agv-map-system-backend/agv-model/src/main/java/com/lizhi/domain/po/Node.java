package com.lizhi.domain.po;

import java.io.Serializable;
import java.util.Objects;

/**
 * 寻路节点类，封装坐标x和y, 移动代价cost和父节点索引
 */
public class Node implements Serializable {
    private final int x;        // x坐标
    private final int y;        // y坐标
    //private double f;           // 从初始状态经由状态n到目标状态的代价估计
    private double g;           // 在状态空间中从初始状态到状态n的实际代价
    private double h;           // 从状态n到目标状态的最佳路径的估计代价
    private double cost;           // 移动代价
    private Integer direction;
    private Node parent;        // 父节点，用于路径回溯

    public Node(int x, int y, double cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

/*    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }*/

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    /**
     * 重写equals方法，判断两个节点是否相等
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;//判断是否为同一对象
        if (!(obj instanceof Node)) return false;//判断obj是否为Node对象
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }


    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
