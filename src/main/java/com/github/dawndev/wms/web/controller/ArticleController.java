package com.github.dawndev.wms.web.controller;

import com.github.dawndev.wms.common.domain.SystemConstant;
import com.github.dawndev.wms.common.domain.SimpleResponse;
import com.github.dawndev.wms.common.exception.WmsException;
import com.github.dawndev.wms.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("article")
public class ArticleController {

    @GetMapping
    @RequiresPermissions("article:view")
    public SimpleResponse queryArticle(String date) throws WmsException {
        String param;
        String data;
        try {
            if (StringUtils.isNotBlank(date)) {
                param = "dev=1&date=" + date;
                data = HttpUtil.sendSSLPost(SystemConstant.MRYW_DAY_URL, param);
            } else {
                param = "dev=1";
                data = HttpUtil.sendSSLPost(SystemConstant.MRYW_TODAY_URL, param);
            }
            return new SimpleResponse().data(data);
        } catch (Exception e) {
            String message = "获取文章失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }
}
