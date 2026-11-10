package com.lizhi.astart.service;



import com.lizhi.astart.heuristic.Euclidean;
import com.lizhi.astart.heuristic.Manhattan;
import com.lizhi.astart.map.GridMap;
import com.lizhi.domain.po.Node;

import java.util.*;

/**
 * A*算法核心实现
 */
public class AStarSolver {

    private final GridMap map;

    public AStarSolver(GridMap map) {
        this.map = map;
    }


    public List<Node> AstarFindPath(Node start, Node goal) throws NoPathFoundException{
        HashSet<Node> openList = new HashSet<>();//需要关注的集合
        HashSet<Node> closedList = new HashSet<>();//不许要关注的集合
        start.setCost(0);
        openList.add(start);
        while (!openList.isEmpty()){
            Node minCostNode = selectMinCost(openList);
            openList.remove(minCostNode);
            closedList.add(minCostNode);
            if (minCostNode.equals(goal)){
                return reconstructPath(minCostNode);
            }
            List<Node> neighbors = map.getNeighbors(minCostNode, goal);
            for (Node neighbor : neighbors) {
                if (closedList.contains(neighbor)){
                    continue;
                } else if (openList.contains(neighbor)) {
                    Node existingNode = openList.stream()
                            .filter(node -> node.equals(neighbor))
                            .findFirst()
                            .orElse(neighbor);
                    double sumCost = calculationCost(start,minCostNode,goal) + calculationCost(minCostNode,neighbor,goal);
                    double sumOldCost = calculationCost(start,existingNode.getParent(),goal) + calculationCost(existingNode.getParent(),neighbor,goal);
                    if (sumCost < sumOldCost){
                        neighbor.setParent(minCostNode);
                        calculationCost(minCostNode,neighbor,goal);
                    }else {
                        continue;
                    }
                }else {
                    neighbor.setParent(minCostNode);
                    calculationCost(minCostNode,neighbor,goal);
                    openList.add(neighbor);
                }
            }
        }
        throw new NoPathFoundException("未找到可达路径");
    }

    private double calculationCost(Node current,Node next,Node goal){
        Manhattan manhattan = new Manhattan();
        Euclidean euclidean = new Euclidean();
        int dx = Math.abs(next.getX() - current.getX());
        int dy = Math.abs(next.getY() - current.getY());
        if (dx == dy && dx != 0){
            next.setG(euclidean.estimate(current, next));
        }else {
            next.setG(manhattan.estimate(current, next));
        }
        next.setH(manhattan.estimate(next, goal));
        double cost = next.getG() + next.getH();
        next.setCost(cost);
        return cost;
    }

    /**
     * 回溯父节点生成路径
     * @param end
     * @return
     */
    private List<Node> reconstructPath(Node end){
        ArrayList<Node> path = new ArrayList<>();//路径
        Node current = end;//当前节点
        //循环获取父节点
        while(current != null){
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);//反转路径
        return path;
    }

    /**
     * 打印地图，包含起点、终点和最优路径
     * @param map 地图对象
     * @param path 最优路径
     * @param start 起点
     * @param goal 终点
     */
    public void printMapWithPath(GridMap map, List<Node> path, Node start, Node goal) {
        int width = map.getWidth();
        int height = map.getHeight();

        // 创建一个二维数组来表示地图状态
        char[][] displayMap = new char[height][width];

        // 初始化地图：'.'表示可通行，'#'表示障碍物
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map.getWeight(x,y) < 0) {
                    displayMap[y][x] = '#';  // 障碍物
                } else {
                    displayMap[y][x] = '.';  // 可通行
                }
            }
        }

        // 标记路径点
        if (path != null) {
            for (Node node : path) {
                // 不覆盖起点和终点的标记
                if (!node.equals(start) && !node.equals(goal)) {
                    displayMap[node.getY()][node.getX()] = '*';  // 路径点
                }
            }
        }

        // 标记起点和终点
        displayMap[start.getY()][start.getX()] = 'S';  // 起点
        displayMap[goal.getY()][goal.getX()] = 'G';    // 终点

        // 打印地图
        System.out.println("地图 (S=起点, G=终点, *=路径, #=障碍物, .=可通行):");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(displayMap[y][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 从openList中选择具有最小f值的节点（A*算法标准做法）
     * @param openList 开放节点集合
     * @return f值最小的节点
     */
    private Node selectMinCost(HashSet<Node> openList) {
        if (openList == null || openList.isEmpty()) {
            return null;
        }

        Iterator<Node> iterator = openList.iterator();
        Node minCostNode = iterator.next();
        double minCost = minCostNode.getCost(); // 使用节点的f值（g+h）

        // 遍历openList中的所有节点，找到f值最小的节点
        while (iterator.hasNext()) {
            Node node = iterator.next();
            double fValue = node.getCost();

            if (fValue < minCost) {
                minCost = fValue;
                minCostNode = node;
            }
        }

        return minCostNode;
    }



}
