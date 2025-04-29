package com.github.dawndev.wms.cos.service.impl;

import com.github.dawndev.wms.cos.entity.StockPut;
import com.github.dawndev.wms.cos.dao.StockPutMapper;
import com.github.dawndev.wms.cos.service.IStockPutService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Espen
 */
@Service
public class StockPutServiceImpl extends ServiceImpl<StockPutMapper, StockPut> implements IStockPutService {

    @Override
    public IPage<LinkedHashMap<String, Object>> stockPutByPage(Page page, StockPut stockPut) {
        return baseMapper.stockPutByPage(page, stockPut);
    }
}
