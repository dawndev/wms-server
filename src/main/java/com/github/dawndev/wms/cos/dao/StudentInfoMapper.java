package com.github.dawndev.wms.cos.dao;

import com.github.dawndev.wms.cos.entity.StudentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Espen
 */
public interface StudentInfoMapper extends BaseMapper<StudentInfo> {

    // 分页获取用户信息
    IPage<LinkedHashMap<String, Object>> studentInfoByPage(Page page, @Param("studentInfo") StudentInfo studentInfo);
}
