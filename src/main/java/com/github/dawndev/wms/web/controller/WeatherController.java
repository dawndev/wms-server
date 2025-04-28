package com.github.dawndev.wms.web.controller;

import com.github.dawndev.wms.common.domain.FebsConstant;
import com.github.dawndev.wms.common.domain.FebsResponse;
import com.github.dawndev.wms.common.exception.FebsException;
import com.github.dawndev.wms.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("weather")
public class WeatherController {

    @GetMapping
    @RequiresPermissions("weather:view")
    public FebsResponse queryWeather(@NotBlank(message = "{required}") String areaId) throws FebsException {
        try {
            String data = HttpUtil.sendPost(FebsConstant.MEIZU_WEATHER_URL, "cityIds=" + areaId);
            return new FebsResponse().data(data);
        } catch (Exception e) {
            String message = "天气查询失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
