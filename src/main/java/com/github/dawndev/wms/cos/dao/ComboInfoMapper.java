package com.github.dawndev.wms.cos.dao;

import com.github.dawndev.wms.cos.entity.ComboInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface ComboInfoMapper extends BaseMapper<ComboInfo> {

    // 分页获取实验套餐信息
    IPage<LinkedHashMap<String, Object>> comboInfoByPage(Page page, @Param("comboInfo") ComboInfo comboInfo);
}
