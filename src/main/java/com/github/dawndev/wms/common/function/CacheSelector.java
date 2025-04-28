package com.github.dawndev.wms.common.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
