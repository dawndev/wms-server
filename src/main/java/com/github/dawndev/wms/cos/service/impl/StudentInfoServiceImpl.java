package com.github.dawndev.wms.cos.service.impl;

import com.github.dawndev.wms.cos.entity.StudentInfo;
import com.github.dawndev.wms.cos.dao.StudentInfoMapper;
import com.github.dawndev.wms.cos.service.IStudentInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo> implements IStudentInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> studentInfoByPage(Page page, StudentInfo studentInfo) {
        return baseMapper.studentInfoByPage(page, studentInfo);
    }
}
