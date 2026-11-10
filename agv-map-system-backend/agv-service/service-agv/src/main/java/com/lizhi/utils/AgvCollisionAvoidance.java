package com.lizhi.utils;

import com.lizhi.domain.po.AgvShape;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgvCollisionAvoidance {

    static class Coordinate{
        Integer x;
        Integer y;

        public Coordinate(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Range{
        Integer min;
        Integer max;

        public Range(Integer min, Integer max) {
            this.min = min;
            this.max = max;
        }
    }

    public AgvCollisionAvoidance() {
    }

    /**
     * 计算agv小车四角坐标
     * @return
     */
    private List<Coordinate> vertices(AgvShape agvShape){
        ArrayList<Coordinate> list = new ArrayList<>();
        double rad = agvShape.getAngle() * Math.PI / 180;
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        int halfWidth = agvShape.getWidth() / 2;
        int halfHeight = agvShape.getHeight() / 2;
        list.add(new Coordinate((int) (agvShape.getX() + cos * halfWidth - sin * halfHeight), (int) (agvShape.getY() + sin*halfWidth + cos*halfHeight)));
        list.add(new Coordinate((int) (agvShape.getX() - cos * halfWidth - sin * halfHeight), (int) (agvShape.getY() - sin*halfWidth + cos*halfHeight)));
        list.add(new Coordinate((int) (agvShape.getX() - cos * halfWidth + sin * halfHeight), (int) (agvShape.getY() - sin*halfWidth - cos*halfHeight)));
        list.add(new Coordinate((int) (agvShape.getX() + cos * halfWidth + sin * halfHeight), (int) (agvShape.getY() + sin*halfWidth - cos*halfHeight)));
        return list;
    }

    /**
     * 计算两边的向量
     * @return
     */
    private List<Coordinate> axes(List<Coordinate> vertices){
        ArrayList<Coordinate> list = new ArrayList<>();
        list.add(new Coordinate(vertices.get(1).x - vertices.get(0).x,vertices.get(1).y - vertices.get(0).y));
        list.add(new Coordinate(vertices.get(2).x - vertices.get(1).x,vertices.get(2).y - vertices.get(1).y));
        return list;
    }

    /**
     * 获取四个边界在向量上投影的最大值和最小值
     * @return
     */
    private Range range(Coordinate axis,List<Coordinate> vertices){
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Coordinate vertex : vertices) {
            int dot = vertex.x * axis.x + vertex.y * axis.y;//计算两向量点积，一个向量在另一个向量上的投影
            min = Math.min(min,dot);
            max = Math.max(max,dot);
        }
        return new Range(min,max);
    }

    public boolean collidesWith(AgvShape agvShape1,AgvShape agvShape2){
        List<Coordinate> vertices1 = vertices(agvShape1);//小车1，四角坐标
        List<Coordinate> vertices2 = vertices(agvShape2);//小车1，四角坐标
        List<Coordinate> axes1 = axes(vertices1);//小车1，两边向量
        List<Coordinate> axes2 = axes(vertices2);//小车1，两边向量
        axes1.addAll(axes2);
        int collision = 1;
        for (Coordinate axes : axes1) {
            Range range1 = range(axes, vertices1);
            Range range2 = range(axes, vertices2);
            if (range2.max < range1.min || range1.max < range2.min){
                collision = 0;
            }
        }
        return collision == 1;
    }

}
