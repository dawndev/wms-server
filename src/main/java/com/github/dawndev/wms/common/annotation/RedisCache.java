package com.github.dawndev.wms.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    String key(); // 缓存键（支持SpEL表达式，如 "#user.id"）
    long ttl() default 3600; // 过期时间（秒）
    Class<?> type(); // 返回值类型（用于反序列化）
    boolean cacheNull() default false; // 是否缓存null值
}
