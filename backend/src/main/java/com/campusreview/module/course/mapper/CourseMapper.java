package com.campusreview.module.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusreview.module.course.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    @Select("SELECT c.*, m.name AS major_name, co.name AS college_name " +
            "FROM course c " +
            "LEFT JOIN major m ON c.major_id = m.id " +
            "LEFT JOIN college co ON m.college_id = co.id " +
            "WHERE c.id = #{id}")
    Course selectDetailById(Long id);
}
