package com.github.dawndev.wms.common.exception;

/**
 * 系统内部异常
 */
public class WmsException extends Exception {

    private static final long serialVersionUID = -994962710559017255L;

    public WmsException(String message) {
        super(message);
    }
}
