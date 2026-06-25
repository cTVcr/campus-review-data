package com.campusreview.module.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusreview.common.constant.ResultCode;
import com.campusreview.common.exception.BusinessException;
import com.campusreview.module.course.entity.Course;
import com.campusreview.module.course.mapper.CourseMapper;
import com.campusreview.module.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    @Override
    public Page<Course> pageQuery(int page, int size, Long majorId, String keyword) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (majorId != null) {
            wrapper.eq(Course::getMajorId, majorId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Course::getName, keyword).or().like(Course::getTeacher, keyword));
        }
        wrapper.orderByAsc(Course::getName);
        return courseMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public List<Course> getByMajorId(Long majorId) {
        return courseMapper.selectList(
                new LambdaQueryWrapper<Course>()
                        .eq(Course::getMajorId, majorId)
                        .orderByAsc(Course::getName));
    }

    @Override
    public Course getDetail(Long id) {
        Course course = courseMapper.selectDetailById(id);
        if (course == null) {
            throw new BusinessException(ResultCode.COURSE_NOT_FOUND);
        }
        return course;
    }
}
