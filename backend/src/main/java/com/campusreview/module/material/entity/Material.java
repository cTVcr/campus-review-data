package com.campusreview.module.material.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("material")
public class Material {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private Long courseId;

    private String type;

    private String fileUrl;

    private String fileName;

    private Long fileSize;

    private String fileType;

    private Integer pageCount;

    private Integer year;

    private String semester;

    private Long uploaderId;

    private Integer viewCount;

    private Integer likeCount;

    private Integer downloadCount;

    private Integer commentCount;

    private String status;

    /** 联表字段 */
    @TableField(exist = false)
    private String uploaderName;

    @TableField(exist = false)
    private String courseName;

    @TableField(exist = false)
    private Boolean liked;

    @TableField(exist = false)
    private Boolean favorited;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
