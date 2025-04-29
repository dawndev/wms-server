package com.github.dawndev.wms.system.service.impl;

import com.github.dawndev.wms.common.annotation.Log;
import com.github.dawndev.wms.common.domain.SystemConstant;
import com.github.dawndev.wms.common.domain.QueryRequest;
import com.github.dawndev.wms.common.utils.AddressUtil;
import com.github.dawndev.wms.common.utils.SortUtil;
import com.github.dawndev.wms.system.dao.LogMapper;
import com.github.dawndev.wms.system.domain.SysLog;
import com.github.dawndev.wms.system.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Service("logService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends ServiceImpl<LogMapper, SysLog> implements LogService {

    @Autowired
    ObjectMapper objectMapper;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public IPage<SysLog> findLogs(QueryRequest request, SysLog sysLog) {
        try {
            QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotBlank(sysLog.getUsername())) {
                queryWrapper.lambda().eq(SysLog::getUsername, sysLog.getUsername().toLowerCase());
            }
            if (StringUtils.isNotBlank(sysLog.getOperation())) {
                queryWrapper.lambda().like(SysLog::getOperation, sysLog.getOperation());
            }
            if (StringUtils.isNotBlank(sysLog.getLocation())) {
                queryWrapper.lambda().like(SysLog::getLocation, sysLog.getLocation());
            }
            if (StringUtils.isNotBlank(sysLog.getCreateTimeFrom()) && StringUtils.isNotBlank(sysLog.getCreateTimeTo())) {
                queryWrapper.lambda()
                        .ge(SysLog::getCreateTime, sysLog.getCreateTimeFrom())
                        .le(SysLog::getCreateTime, sysLog.getCreateTimeTo());
            }

            Page<SysLog> page = new Page<>(request.getPageNum(), request.getPageSize());
            SortUtil.handlePageSort(request, page, "createTime", SystemConstant.ORDER_DESC, true);

            return this.page(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取系统日志失败", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteLogs(String[] logIds) {
        List<String> list = Arrays.asList(logIds);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void saveAsyncLog(ProceedingJoinPoint joinPoint, SysLog log) throws JsonProcessingException {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            String operation = parseSpel(logAnnotation.value(), joinPoint);
            log.setOperation(operation);
//            log.setType(logAnnotation.type());
        }
        // 请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            log.setParams(params.toString());
        }
        log.setCreateTime(new Date());
        log.setLocation(AddressUtil.getCityInfo(log.getIp()));
        // 保存系统日志
        save(log);
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) throws JsonProcessingException {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                Set set = ((Map) args[i]).keySet();
                List<Object> list = new ArrayList<>();
                List<Object> paramList = new ArrayList<>();
                for (Object key : set) {
                    list.add(((Map) args[i]).get(key));
                    paramList.add(key);
                }
                return handleParams(params, list.toArray(), paramList);
            } else {
                if (args[i] instanceof Serializable) {
                    Class<?> aClass = args[i].getClass();
                    try {
                        aClass.getDeclaredMethod("toString", new Class[]{null});
                        // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                    } catch (NoSuchMethodException e) {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                    }
                } else if (args[i] instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) args[i];
                    params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                } else {
                    params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                }
            }
        }
        return params;
    }

    private String parseSpel(String expr, ProceedingJoinPoint joinPoint) {
        if (!expr.contains("#")) return expr;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", joinPoint.getArgs());

        // 设置参数名（如 #{#user.id} 中的 user）
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        return parser.parseExpression(expr).getValue(context, String.class);
    }
}


