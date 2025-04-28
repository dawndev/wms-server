package com.github.dawndev.wms.cos.controller;


import com.github.dawndev.wms.common.utils.R;
import com.github.dawndev.wms.cos.entity.StockPut;
import com.github.dawndev.wms.cos.service.IStockPutService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/stock-put")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockPutController {

    private final IStockPutService stockPutService;

    /**
     * 分页获取入库记录
     * @param page
     * @param stockPut
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, StockPut stockPut) {
        return R.ok(stockPutService.stockPutByPage(page, stockPut));
    }

    /**
     * 删除入库记录
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(stockPutService.removeByIds(ids));
    }

}
