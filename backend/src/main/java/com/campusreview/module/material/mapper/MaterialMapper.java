package com.campusreview.module.material.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusreview.module.material.entity.Material;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MaterialMapper extends BaseMapper<Material> {

    @Select("<script>" +
            "SELECT m.*, u.nickname AS uploader_name, c.name AS course_name " +
            "FROM material m " +
            "LEFT JOIN sys_user u ON m.uploader_id = u.id " +
            "LEFT JOIN course c ON m.course_id = c.id " +
            "WHERE m.status = 'PUBLISHED' " +
            "<if test='query.courseId != null'>AND m.course_id = #{query.courseId}</if> " +
            "<if test='query.type != null and query.type != \"\"'>AND m.type = #{query.type}</if> " +
            "<if test='query.year != null'>AND m.year = #{query.year}</if> " +
            "<if test='query.keyword != null and query.keyword != \"\"'>" +
            "  AND (m.title LIKE CONCAT('%',#{query.keyword},'%') OR m.description LIKE CONCAT('%',#{query.keyword},'%')) " +
            "</if> " +
            "ORDER BY " +
            "<choose>" +
            "  <when test='query.sort == \"popular\"'>m.like_count DESC</when>" +
            "  <when test='query.sort == \"downloads\"'>m.download_count DESC</when>" +
            "  <otherwise>m.created_at DESC</otherwise>" +
            "</choose>" +
            "</script>")
    List<Material> selectMaterialList(Page<Material> page, @Param("query") Material query);
}
