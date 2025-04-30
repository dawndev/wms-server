//package com.github.dawndev.wms.common.aspect;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.dawndev.wms.common.annotation.RedisCache;
//import com.github.dawndev.wms.common.annotation.RedisCacheEvict;
//import com.github.dawndev.wms.common.exception.RedisConnectException;
//import com.github.dawndev.wms.common.service.RedisService;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.expression.EvaluationContext;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//import org.springframework.stereotype.Component;
//import java.util.concurrent.CompletableFuture;
//
//@Slf4j
//@Aspect
//@Component
//public class RedisCacheAspect {
//
//    @Autowired
//    private RedisService redisService;
////    private final RedisTemplate<String, String> redisTemplate;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final SpelExpressionParser spelParser = new SpelExpressionParser();
//
//    // 环绕通知：处理缓存逻辑
//    @Around("@annotation(redisCache)")
//    public Object around(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
//        // 1. 安全解析缓存键
//        String cacheKey = parseKey(redisCache.key(), joinPoint);
//
//        try {
//            // 2. 尝试读缓存
//            String cachedValue = redisService.get(cacheKey);
//            if (cachedValue != null) {
//                if ("NULL".equals(cachedValue) && redisCache.cacheNull()) {
//                    return null;
//                }
//                return deserialize(cachedValue, redisCache.type());
//            }
//
//            // 3. 执行原方法
//            Object result = joinPoint.proceed();
//
//            // 4. 异步写缓存（不阻塞主流程）
//            if (shouldCache(result, redisCache.cacheNull())) {
//                asyncCacheWrite(cacheKey, result, redisCache.ttl());
//            }
//            return result;
//
//        } catch (RedisConnectException e) {
//            log.warn("Redis unavailable, bypassing cache for key: {}", cacheKey);
//            return joinPoint.proceed(); // 降级
//        }
//    }
//
//    // 解析SpEL表达式
//    private String parseKey(String keyExpr, JoinPoint joinPoint) {
//        // 获取方法参数名和值
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        String[] paramNames = signature.getParameterNames();
//        Object[] args = joinPoint.getArgs();
//
//        // 构建SpEL上下文
//        EvaluationContext context = new StandardEvaluationContext();
//        for (int i = 0; i < paramNames.length; i++) {
//            context.setVariable(paramNames[i], args[i]);
//        }
//
//        // 解析表达式
//        return new SpelExpressionParser()
//                .parseExpression(keyExpr)
//                .getValue(context, String.class);
//    }
//
//    // 清除缓存（可用于更新或删除操作）
//    @AfterReturning("@annotation(redisCacheEvict)")
//    public void evictCache(RedisCacheEvict redisCacheEvict, JoinPoint joinPoint) throws RedisConnectException {
//        try {
//            String cacheKey = parseKey(redisCacheEvict.key(), joinPoint);
//            if (redisCacheEvict.allEntries()) {
//                redisService.delPattern(cacheKey + "*"); // 模糊删除
//            } else {
//                redisService.del(cacheKey);
//            }
//        } catch (Exception e) {
//            log.error("Cache eviction failed", e);
//        }
//    }
//
//
//    private Object deserialize(String value, Class<?> type) throws JsonProcessingException {
//        return objectMapper.readValue(value, objectMapper.constructType(type));
//    }
//
//    private boolean shouldCache(Object result, boolean cacheNull) {
//        return result != null || cacheNull;
//    }
//
//    private void asyncCacheWrite(String key, Object value, long ttl) {
//        CompletableFuture.runAsync(() -> {
//            try {
//                String serialized = (value != null) ?
//                        objectMapper.writeValueAsString(value) : "NULL";
//                redisService.set(key, serialized, ttl);
//            } catch (Exception e) {
//                log.error("Async cache write failed", e);
//            }
//        });
//    }
//}
//
