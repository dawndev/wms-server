package com.github.dawndev.wms.cos.service;

import com.github.dawndev.wms.cos.entity.StockPut;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Espen
 */
public interface IStockPutService extends IService<StockPut> {

    // 分页获取入库记录
    IPage<LinkedHashMap<String, Object>> stockPutByPage(Page page, StockPut stockPut);
}
