package com.github.dawndev.wms.system.service;

import com.github.dawndev.wms.common.domain.SimpleResponse;
import com.github.dawndev.wms.system.domain.LoginLog;
import com.github.dawndev.wms.system.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog (LoginLog loginLog);

    SimpleResponse faceLogin(User user, HttpServletRequest request) throws Exception;
}
