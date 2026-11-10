package com.lizhi.astart.service;

import com.lizhi.domain.po.AgvLog;

import java.time.LocalDateTime;


/**
 * 无路径异常
 */
public class NoPathFoundException extends Exception{
    public NoPathFoundException(String message){
        super(message);
    }
}
