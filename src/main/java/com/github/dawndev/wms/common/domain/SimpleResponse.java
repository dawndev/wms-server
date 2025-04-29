package com.github.dawndev.wms.common.domain;

import java.util.HashMap;

/**
 * 响应
 */
public class SimpleResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public SimpleResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public SimpleResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public SimpleResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
