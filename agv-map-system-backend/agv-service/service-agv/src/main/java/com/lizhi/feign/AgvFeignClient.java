package com.lizhi.feign;

import com.lizhi.astart.service.NoPathFoundException;
import com.lizhi.domain.po.AgvSnapshot;
import com.lizhi.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "service-agv")
public interface AgvFeignClient {

    /**
     * 远程调用agv小车充电
     */
    @PostMapping("/agv/charge/{agvCode}")
    void chargeAgv(@PathVariable("agvCode") String agvCode) throws NoPathFoundException;
}
