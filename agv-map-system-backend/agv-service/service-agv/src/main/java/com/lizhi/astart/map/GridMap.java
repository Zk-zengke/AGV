package com.lizhi.astart.map;



import com.lizhi.domain.po.Node;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class GridMap {
    private final int width;//地图的宽度
    private final int height;//地图的高度
    private final double[][] weight;    // 每个格子的移动代价值
    private final boolean allowDiagonal; // 是否允许对角移动

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }



    /**
     * 创建地图
     * @param allowDiagonal 是否允许对角移动
     * @param height 地图的高度
     * @param width 地图的宽度
     */
    public GridMap(boolean allowDiagonal, int height, int width) {
        this.allowDiagonal = allowDiagonal;
        this.height = height;
        this.width = width;
        this.weight = new double[height][width];
        // 默认所有格子代价为1，可通过 setWeight 调整
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                weight[i][j] = 1.0;
            }
        }

    }

    /**
     * 设置某个格子的代价
     * @param x
     * @param y
     * @param w
     */
    public void setWeight(int x,int y,double w){
        if (inBounds(x,y)) weight[y][x] = w;
    }
    //获得地图的某个格子的代价
    public double getWeight(int x,int y){
        return weight[y][x];
    }

    /**
     * 判断某个格子是否在边界内
     * @param x
     * @param y
     * @return
     */
    private boolean inBounds(int x,int y){
        return x >=0 && x < width && y >= 0 && y < height;
    }

    /**
     * 获取某个格子的相邻格子
     * @param current
     * @return heuristic
     */
    public List<Node> getNeighbors(Node current, Node goal){
        ArrayList<Node> rectangleNeighbors = new ArrayList<>();
        ArrayList<Node> diagonallyNeighbors = new ArrayList<>();
        int x = current.getX();
        int y = current.getY();
        //四方向
        addIfValid(rectangleNeighbors,x+1,y);
        addIfValid(rectangleNeighbors,x-1,y);
        addIfValid(rectangleNeighbors,x,y+1);
        addIfValid(rectangleNeighbors,x,y-1);
        //对角
        if (allowDiagonal){
            addIfValid(diagonallyNeighbors,x+1,y+1);
            addIfValid(diagonallyNeighbors,x+1,y-1);
            addIfValid(diagonallyNeighbors,x-1,y+1);
            addIfValid(diagonallyNeighbors,x-1,y-1);
        }
        if (!diagonallyNeighbors.isEmpty()){
            rectangleNeighbors.addAll(diagonallyNeighbors);
        }
        return rectangleNeighbors;
    }



    /**
     * 添加一个格子到列表中
     * @param list
     * @param x
     * @param y
     */
    private void addIfValid(List<Node> list,int x,int y){
        if(!inBounds(x,y)) return;
        if(weight[y][x] < 0) return;//负值表示障碍
        list.add(new Node(x,y));
    }
}
