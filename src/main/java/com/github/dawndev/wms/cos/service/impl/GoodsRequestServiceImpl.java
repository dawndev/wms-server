package com.github.dawndev.wms.cos.service.impl;

import com.github.dawndev.wms.cos.entity.GoodsRequest;
import com.github.dawndev.wms.cos.dao.GoodsRequestMapper;
import com.github.dawndev.wms.cos.service.IGoodsRequestService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Espen
 */
@Service
public class GoodsRequestServiceImpl extends ServiceImpl<GoodsRequestMapper, GoodsRequest> implements IGoodsRequestService {

    @Override
    public IPage<LinkedHashMap<String, Object>> goodsRequestByPage(Page page, GoodsRequest goodsRequest) {
        return baseMapper.goodsRequestByPage(page, goodsRequest);
    }
}
