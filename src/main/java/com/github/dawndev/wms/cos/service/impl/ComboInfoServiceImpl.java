package com.github.dawndev.wms.cos.service.impl;

import com.github.dawndev.wms.cos.entity.ComboInfo;
import com.github.dawndev.wms.cos.dao.ComboInfoMapper;
import com.github.dawndev.wms.cos.service.IComboInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class ComboInfoServiceImpl extends ServiceImpl<ComboInfoMapper, ComboInfo> implements IComboInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> comboInfoByPage(Page page, ComboInfo comboInfo) {
        return baseMapper.comboInfoByPage(page, comboInfo);
    }
}
