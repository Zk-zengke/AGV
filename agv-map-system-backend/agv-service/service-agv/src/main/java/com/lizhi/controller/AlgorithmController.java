package com.lizhi.controller;

import com.lizhi.astart.timewindow.AlgorithmModeManager;
import com.lizhi.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 调度算法模式切换：A* 空间占点避让 与 时间窗冲突避障 可在运行时切换。
 */
@RestController
@RequestMapping("/algorithm")
@RequiredArgsConstructor
public class AlgorithmController {

    private final AlgorithmModeManager algorithmModeManager;

    @GetMapping("/mode")
    public Result<Map<String, String>> getMode() {
        Map<String, String> data = new HashMap<>();
        data.put("mode", algorithmModeManager.getMode());
        return Result.success(data);
    }

    @PostMapping("/mode/{mode}")
    public Result<Map<String, String>> setMode(@PathVariable String mode) {
        boolean ok = algorithmModeManager.setMode(mode);
        if (!ok) {
            return Result.error("不支持的算法模式：" + mode);
        }
        Map<String, String> data = new HashMap<>();
        data.put("mode", algorithmModeManager.getMode());
        return Result.success(data);
    }
}
