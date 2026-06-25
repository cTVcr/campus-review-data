package com.campusreview.module.course.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String code;

    private Long majorId;

    private String teacher;

    private BigDecimal credit;

    private String semester;

    private String description;

    private String coverImage;

    private Integer materialCount;

    @TableField(exist = false)
    private String majorName;

    @TableField(exist = false)
    private String collegeName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
