package com.campusreview.module.comment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long materialId;

    private Long userId;

    private String content;

    private Long parentId;

    private Integer likeCount;

    private Integer status;

    /** 联表 */
    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String avatarUrl;

    @TableField(exist = false)
    private java.util.List<Comment> replies;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
