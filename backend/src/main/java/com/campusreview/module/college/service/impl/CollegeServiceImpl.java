package com.campusreview.module.college.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campusreview.common.constant.ResultCode;
import com.campusreview.common.exception.BusinessException;
import com.campusreview.module.college.entity.College;
import com.campusreview.module.college.entity.Major;
import com.campusreview.module.college.mapper.CollegeMapper;
import com.campusreview.module.college.mapper.MajorMapper;
import com.campusreview.module.college.service.CollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements CollegeService {

    private final CollegeMapper collegeMapper;
    private final MajorMapper majorMapper;

    @Override
    public List<College> listAll() {
        return collegeMapper.selectList(
                new LambdaQueryWrapper<College>().orderByAsc(College::getSortOrder));
    }

    @Override
    public College getById(Long id) {
        College college = collegeMapper.selectById(id);
        if (college == null) {
            throw new BusinessException(ResultCode.COLLEGE_NOT_FOUND);
        }
        return college;
    }

    @Override
    public List<Major> getMajorsByCollegeId(Long collegeId) {
        return majorMapper.selectList(
                new LambdaQueryWrapper<Major>()
                        .eq(Major::getCollegeId, collegeId)
                        .orderByAsc(Major::getSortOrder));
    }
}
