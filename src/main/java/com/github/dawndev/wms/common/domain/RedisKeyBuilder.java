package com.github.dawndev.wms.common.domain;

public class RedisKeyBuilder {

    private static final String PREFIX = "wms";

    // 用户缓存
    public static String user(String username) {
        return String.format("%s:cache:user:%s", PREFIX, username);
    }

    // 用户角色缓存
    public static String role(String username) {
        return String.format("%s:cache:user:role:%s", PREFIX, username);
    }

    // 用户权限
    public static String permission(String username) {
        return String.format("%s:cache:user:permission:%s", PREFIX, username);
    }

    // 用户个性化配置
    public static String config(String userId) {
        return String.format("%s:cache:user:config:%s", PREFIX, userId);
    }

    // 用户token
    public static String userToken(String token, String ip) {
        return String.format("%s:auth:token:%s:%s", PREFIX, token, ip);
    }

    // 存储在线用户的 zset
    public static String stockItem() {
        return String.format("%s:user:active", PREFIX);
    }
}
