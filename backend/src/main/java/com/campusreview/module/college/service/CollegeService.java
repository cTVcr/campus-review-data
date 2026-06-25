package com.campusreview.module.college.service;

import com.campusreview.module.college.entity.College;
import com.campusreview.module.college.entity.Major;

import java.util.List;

public interface CollegeService {
    List<College> listAll();
    College getById(Long id);
    List<Major> getMajorsByCollegeId(Long collegeId);
}
