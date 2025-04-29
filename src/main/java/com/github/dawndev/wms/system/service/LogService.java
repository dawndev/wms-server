package com.github.dawndev.wms.system.service;

import com.github.dawndev.wms.common.domain.QueryRequest;
import com.github.dawndev.wms.system.domain.SysLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.scheduling.annotation.Async;

/**
 * 日志记录Service
 */
public interface LogService extends IService<SysLog> {

    IPage<SysLog> findLogs(QueryRequest request, SysLog sysLog);

    void deleteLogs(String[] logIds);

    @Async
    void saveAsyncLog(ProceedingJoinPoint point, SysLog log) throws JsonProcessingException;
}
