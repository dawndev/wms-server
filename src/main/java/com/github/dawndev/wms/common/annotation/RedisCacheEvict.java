package com.github.dawndev.wms.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheEvict {
    String key(); // 要清除的缓存键（支持SpEL）
    boolean allEntries() default false; // 是否清空所有相关缓存
}
