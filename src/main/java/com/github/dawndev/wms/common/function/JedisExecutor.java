package com.github.dawndev.wms.common.function;

import com.github.dawndev.wms.common.exception.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R execute(T t) throws RedisConnectException;
}
