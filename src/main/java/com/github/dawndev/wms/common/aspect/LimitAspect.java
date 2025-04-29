package com.github.dawndev.wms.common.aspect;

import com.github.dawndev.wms.common.annotation.Limit;
import com.github.dawndev.wms.common.authentication.JWTUtil;
import com.github.dawndev.wms.common.domain.LimitType;
import com.github.dawndev.wms.common.exception.LimitAccessException;
import com.github.dawndev.wms.common.utils.HttpContextUtil;
import com.github.dawndev.wms.common.utils.IPUtil;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;

/**
 * 接口限流的切面
 * <p>
 *     基于 Redis + Lua 实现的分布式限流切面，用于控制接口访问频率
 * </p>
 * <p>
 *      优势：分布式限流、低延迟（Lua脚本原子化执行）
 *      优化点：脚本原子性、集群兼容性、监控日志
 *      扩展性：支持多种限流维度和动态规则
 * </p>
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {

    private final RedisTemplate<String, Serializable> redisTemplate;
    private final RedisScript<Long> redisScript;

    public LimitAspect(RedisTemplate<String, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisScript = loadRedisScript();
    }

    @Around("@annotation(limit)")
    public Object around(ProceedingJoinPoint point, Limit limit) throws Throwable {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String actualKey = resolveKey(point, limit, request);
        String prefix = limit.prefix();

        // 集群兼容：使用 {} 保证哈希一致性
        String fullKey = "{" + prefix + "}_" + actualKey;

        Long count = redisTemplate.execute(
                redisScript,
                Collections.singletonList(fullKey),
                limit.count(),
                limit.period()
        );

        if (count == null || count <= limit.count()) {
            return point.proceed();
        }

        log.warn("限流触发：key={}, 计数={}, 限制={}/{}s", fullKey, count, limit.count(), limit.period());
        throw new LimitAccessException("请求过于频繁，请 " + limit.period() + " 秒后再试");
    }

    private String resolveKey(ProceedingJoinPoint point, Limit limit, HttpServletRequest request) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        switch (limit.limitType()) {
            case IP:
                return IPUtil.getIpAddr(request);
            case USER:
                return (String) SecurityUtils.getSubject().getPrincipal();
            case CUSTOMER:
                return limit.key();
            default:
                return StringUtils.upperCase(method.getName());
        }
    }

    private RedisScript<Long> loadRedisScript() {
        String lua = "local key = KEYS[1]\n" +
                "local limit = tonumber(ARGV[1])\n" +
                "local expire = tonumber(ARGV[2])\n\n" +
                "local current = tonumber(redis.call('GET', key) or 0)\n" +
                "if current >= limit then\n" +
                "    return current\n" +
                "end\n\n" +
                "current = redis.call('INCR', key)\n" +
                "if current == 1 then\n" +
                "    redis.call('EXPIRE', key, expire)\n" +
                "end\n" +
                "return current";
        return new DefaultRedisScript<>(lua, Long.class);
    }

    // 辅助方法省略...
}
