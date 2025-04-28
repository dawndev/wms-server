package com.github.dawndev.wms.common.config;

import com.github.dawndev.wms.common.utils.DateUtil;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDateTime;

/**
 * 自定义 p6spy sql输出格式
 */
public class P6spySqlFormatConfig implements MessageFormattingStrategy {

//    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (StringUtils.isBlank(sql)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        sb.append(" | 耗时 ").append(elapsed).append(" ms");
        sb.append(" | 线程ID：").append(Thread.currentThread().getId());
        sb.append(" | SQL 语句：");
        sb.append(StringUtils.LF);
        sb.append(sql.replaceAll("[\\s]+", StringUtils.SPACE));
        sb.append(";");
        return sb.toString();
    }

}
