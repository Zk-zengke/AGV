package com.lizhi.astart.timewindow;

import com.lizhi.astart.map.GridMap;
import com.lizhi.astart.service.NoPathFoundException;
import com.lizhi.domain.po.Node;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 时空 A* 求解器：在 (x, y, t) 三维空间搜索，结合时间窗预约表实现冲突避障。
 * 每个时间步可移动到四邻格或原地等待；启发式只看空间曼哈顿距离。
 */
public class TimeWindowAStar {

    // 时空搜索节点
    private static class TNode {
        final int x;
        final int y;
        final int t;
        final TNode parent;
        final double g;
        final double h;

        TNode(int x, int y, int t, TNode parent, double g, double h) {
            this.x = x;
            this.y = y;
            this.t = t;
            this.parent = parent;
            this.g = g;
            this.h = h;
        }

        double f() {
            return g + h;
        }

        String key() {
            return x + "," + y + "," + t;
        }
    }

    private static final int[][] MOVES = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private final GridMap map;
    private final ReservationTable table;

    public TimeWindowAStar(GridMap map, ReservationTable table) {
        this.map = map;
        this.table = table;
    }

    public List<Node> findPath(Node start, Node goal) throws NoPathFoundException {
        // 搜索时间上限，防止在死锁场景下无限等待
        int horizon = map.getWidth() * map.getHeight() * 2 + 20;
        PriorityQueue<TNode> open = new PriorityQueue<>(Comparator.comparingDouble(TNode::f));
        Set<String> closed = new HashSet<>();

        TNode root = new TNode(start.getX(), start.getY(), 0, null, 0,
                heuristic(start.getX(), start.getY(), goal));
        open.add(root);

        while (!open.isEmpty()) {
            TNode cur = open.poll();
            if (!closed.add(cur.key())) {
                continue;
            }
            if (cur.x == goal.getX() && cur.y == goal.getY()) {
                return reconstruct(cur);
            }
            if (cur.t >= horizon) {
                continue;
            }

            int nextT = cur.t + 1;

            // 移动动作：检查边界、静态障碍、顶点冲突与对向边冲突
            for (int[] move : MOVES) {
                int nx = cur.x + move[0];
                int ny = cur.y + move[1];
                if (!inBounds(nx, ny) || map.getWeight(nx, ny) < 0) {
                    continue;
                }
                if (table.isVertexReserved(nx, ny, nextT)) {
                    continue;
                }
                if (table.isEdgeReserved(nx, ny, cur.x, cur.y, cur.t)) {
                    continue;
                }
                TNode child = new TNode(nx, ny, nextT, cur, cur.g + 1,
                        heuristic(nx, ny, goal));
                if (!closed.contains(child.key())) {
                    open.add(child);
                }
            }

            // 等待动作：原地停留一个时间步
            if (!table.isVertexReserved(cur.x, cur.y, nextT)) {
                TNode wait = new TNode(cur.x, cur.y, nextT, cur, cur.g + 1,
                        heuristic(cur.x, cur.y, goal));
                if (!closed.contains(wait.key())) {
                    open.add(wait);
                }
            }
        }

        throw new NoPathFoundException("时间窗算法未找到可达路径");
    }

    private double heuristic(int x, int y, Node goal) {
        return Math.abs(x - goal.getX()) + Math.abs(y - goal.getY());
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight();
    }

    private List<Node> reconstruct(TNode end) {
        LinkedList<Node> path = new LinkedList<>();
        for (TNode cur = end; cur != null; cur = cur.parent) {
            path.addFirst(new Node(cur.x, cur.y));
        }
        return path;
    }
}
