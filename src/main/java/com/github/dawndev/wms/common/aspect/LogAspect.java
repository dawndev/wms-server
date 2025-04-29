package com.github.dawndev.wms.common.aspect;

import com.github.dawndev.wms.common.authentication.JWTUtil;
import com.github.dawndev.wms.common.properties.WmsProperties;
import com.github.dawndev.wms.common.utils.HttpContextUtil;
import com.github.dawndev.wms.common.utils.IPUtil;
import com.github.dawndev.wms.system.domain.SysLog;
import com.github.dawndev.wms.system.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

/**
 * AOP 记录用户操作日志
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private WmsProperties wmsProperties;

    @Autowired
    private LogService logService;

    // 将日志开关 wmsProperties.isOpenAopLog() 移到切点中，避免每次请求都检查
    @Pointcut("@annotation(com.github.dawndev.wms.common.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        if (wmsProperties.isOpenAopLog()) {
            SysLog log = this.buildSysLog(point, request, beginTime);
            logService.saveAsyncLog(point, log);
        }
        return result;
    }

    private SysLog buildSysLog(ProceedingJoinPoint point, HttpServletRequest request, long beginTime) {
        String ip = IPUtil.getIpAddr(request);
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        String username = StringUtils.isNotBlank(token) ? JWTUtil.getUsername(token) : "";

        SysLog sysLog = new SysLog();
        sysLog.setUsername(username);
        sysLog.setIp(ip);
        sysLog.setTime(System.currentTimeMillis() - beginTime);
//        sysLog.setMethod(point.getSignature().getName());
//        sysLog.setParams(Arrays.toString(point.getArgs()));
        return sysLog;
    }
}
