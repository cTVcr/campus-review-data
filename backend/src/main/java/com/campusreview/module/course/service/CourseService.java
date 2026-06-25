package com.campusreview.module.course.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusreview.module.course.entity.Course;

import java.util.List;

public interface CourseService {
    Page<Course> pageQuery(int page, int size, Long majorId, String keyword);
    List<Course> getByMajorId(Long majorId);
    Course getDetail(Long id);
}
