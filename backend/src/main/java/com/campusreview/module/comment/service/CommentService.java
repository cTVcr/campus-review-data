package com.campusreview.module.comment.service;

import com.campusreview.module.comment.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listByMaterialId(Long materialId);
    Comment add(Long materialId, String content, Long parentId, Long userId);
    void delete(Long materialId, Long commentId, Long userId);
}
