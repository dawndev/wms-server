package com.github.dawndev.wms.web.controller;

import com.github.dawndev.wms.common.domain.SystemConstant;
import com.github.dawndev.wms.common.domain.SimpleResponse;
import com.github.dawndev.wms.common.exception.WmsException;
import com.github.dawndev.wms.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("movie")
public class MovieController {

    private String message;

    @GetMapping("hot")
    public SimpleResponse getMoiveHot() throws WmsException {
        try {
            String data = HttpUtil.sendSSLPost(SystemConstant.TIME_MOVIE_HOT_URL, "locationId=328");
            return new SimpleResponse().data(data);
        } catch (Exception e) {
            message = "获取热映影片信息失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }

    @GetMapping("coming")
    public SimpleResponse getMovieComing() throws WmsException {
        try {
            String data = HttpUtil.sendSSLPost(SystemConstant.TIME_MOVIE_COMING_URL, "locationId=328");
            return new SimpleResponse().data(data);
        } catch (Exception e) {
            message = "获取即将上映影片信息失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }

    @GetMapping("detail")
    public SimpleResponse getDetail(@NotBlank(message = "{required}") String id) throws WmsException {
        try {
            String data = HttpUtil.sendSSLPost(SystemConstant.TIME_MOVIE_DETAIL_URL, "locationId=328&movieId=" + id);
            return new SimpleResponse().data(data);
        } catch (Exception e) {
            message = "获取影片详情失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }

    @GetMapping("comments")
    public SimpleResponse getComments(@NotBlank(message = "{required}") String id) throws WmsException {
        try {
            String data = HttpUtil.sendSSLPost(SystemConstant.TIME_MOVIE_COMMENTS_URL, "movieId=" + id);
            return new SimpleResponse().data(data);
        } catch (Exception e) {
            message = "获取影片评论失败";
            log.error(message, e);
            throw new WmsException(message);
        }
    }
}
