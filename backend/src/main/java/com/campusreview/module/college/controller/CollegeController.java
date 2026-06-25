package com.campusreview.module.college.controller;

import com.campusreview.common.response.Result;
import com.campusreview.module.college.entity.College;
import com.campusreview.module.college.entity.Major;
import com.campusreview.module.college.service.CollegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "学院管理")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService collegeService;

    @Operation(summary = "获取所有学院")
    @GetMapping("/colleges")
    public Result<List<College>> listColleges() {
        return Result.ok(collegeService.listAll());
    }

    @Operation(summary = "获取学院下的专业")
    @GetMapping("/colleges/{id}/majors")
    public Result<List<Major>> listMajors(@PathVariable Long id) {
        return Result.ok(collegeService.getMajorsByCollegeId(id));
    }
}
