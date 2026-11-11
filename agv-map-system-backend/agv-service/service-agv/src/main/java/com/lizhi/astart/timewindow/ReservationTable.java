package com.lizhi.astart.timewindow;

import java.util.HashSet;
import java.util.Set;

/**
 * 时间窗预约表：记录各车辆在每个时间步对格子（顶点）和移动边的占用。
 * 顶点预约用于避免同一时刻多车进入同一格子；边预约用于避免对向交换位置。
 */
public class ReservationTable {

    private final Set<String> vertexReservations = new HashSet<>();
    private final Set<String> edgeReservations = new HashSet<>();

    private static String vKey(int x, int y, int t) {
        return x + "," + y + "," + t;
    }

    private static String eKey(int x1, int y1, int x2, int y2, int t) {
        return x1 + "," + y1 + ">" + x2 + "," + y2 + "@" + t;
    }

    public void reserveVertex(int x, int y, int t) {
        vertexReservations.add(vKey(x, y, t));
    }

    public boolean isVertexReserved(int x, int y, int t) {
        return vertexReservations.contains(vKey(x, y, t));
    }

    public void reserveEdge(int x1, int y1, int x2, int y2, int t) {
        edgeReservations.add(eKey(x1, y1, x2, y2, t));
    }

    // 检查 t 时刻是否已有车辆从 (x1,y1) 移动到 (x2,y2)
    public boolean isEdgeReserved(int x1, int y1, int x2, int y2, int t) {
        return edgeReservations.contains(eKey(x1, y1, x2, y2, t));
    }

    public void clear() {
        vertexReservations.clear();
        edgeReservations.clear();
    }
}
