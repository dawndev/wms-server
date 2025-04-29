package com.github.dawndev.wms.common.domain;

/**
 * 限流维度
 */
public enum LimitType {
    IP,         // 按IP
    USER,       // 按用户ID
    METHOD,     // 按方法
    CUSTOMER    // 自定义
}
