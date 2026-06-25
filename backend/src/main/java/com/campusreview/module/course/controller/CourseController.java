package com.campusreview.module.course.controller;

import com.campusreview.common.response.Result;
import com.campusreview.module.course.entity.Course;
import com.campusreview.module.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "课程管理")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "分页查询课程")
    @GetMapping("/courses")
    public Result<?> listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) String keyword) {
        return Result.ok(courseService.pageQuery(page, size, majorId, keyword));
    }

    @Operation(summary = "获取某专业下的课程")
    @GetMapping("/majors/{majorId}/courses")
    public Result<List<Course>> listByMajor(@PathVariable Long majorId) {
        return Result.ok(courseService.getByMajorId(majorId));
    }

    @Operation(summary = "获取课程详情")
    @GetMapping("/courses/{id}")
    public Result<Course> getDetail(@PathVariable Long id) {
        return Result.ok(courseService.getDetail(id));
    }
}
