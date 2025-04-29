package com.github.dawndev.wms.cos.service.impl;

import com.github.dawndev.wms.cos.entity.GoodsBelong;
import com.github.dawndev.wms.cos.dao.GoodsBelongMapper;
import com.github.dawndev.wms.cos.service.IGoodsBelongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Espen
 */
@Service
public class GoodsBelongServiceImpl extends ServiceImpl<GoodsBelongMapper, GoodsBelong> implements IGoodsBelongService {

    @Override
    public List<LinkedHashMap<String, Object>> getGoodsByNum(String num) {
        return baseMapper.getGoodsByNum(num);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getGoodsDetailByNum(String num) {
        return baseMapper.getGoodsDetailByNum(num);
    }
}
