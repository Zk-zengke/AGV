package com.lizhi.astart.timewindow;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 调度算法模式管理，可在运行时切换。
 * spatial：A* 空间占点避让（默认）；timewindow：时间窗冲突避障。
 */
@Component
public class AlgorithmModeManager {

    public static final String SPATIAL = "spatial";
    public static final String TIME_WINDOW = "timewindow";

    private final AtomicReference<String> mode = new AtomicReference<>(SPATIAL);

    public String getMode() {
        return mode.get();
    }

    public boolean isTimeWindow() {
        return TIME_WINDOW.equals(mode.get());
    }

    public boolean setMode(String newMode) {
        if (SPATIAL.equals(newMode) || TIME_WINDOW.equals(newMode)) {
            mode.set(newMode);
            return true;
        }
        return false;
    }
}
