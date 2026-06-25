package com.campusreview.module.comment.service.impl;

import com.campusreview.common.constant.ResultCode;
import com.campusreview.common.exception.BusinessException;
import com.campusreview.module.comment.entity.Comment;
import com.campusreview.module.comment.mapper.CommentMapper;
import com.campusreview.module.comment.service.CommentService;
import com.campusreview.module.material.entity.Material;
import com.campusreview.module.material.mapper.MaterialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final MaterialMapper materialMapper;

    @Override
    public List<Comment> listByMaterialId(Long materialId) {
        List<Comment> comments = commentMapper.selectByMaterialId(materialId);
        // 加载每条评论的回复
        for (Comment comment : comments) {
            List<Comment> replies = commentMapper.selectReplies(comment.getId());
            comment.setReplies(replies);
        }
        return comments;
    }

    @Override
    @Transactional
    public Comment add(Long materialId, String content, Long parentId, Long userId) {
        Material material = materialMapper.selectById(materialId);
        if (material == null) throw new BusinessException(ResultCode.MATERIAL_NOT_FOUND);

        Comment comment = new Comment();
        comment.setMaterialId(materialId);
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setLikeCount(0);
        comment.setStatus(1);
        commentMapper.insert(comment);

        // 更新资料评论数
        Material update = new Material();
        update.setId(materialId);
        update.setCommentCount(material.getCommentCount() + 1);
        materialMapper.updateById(update);

        return comment;
    }

    @Override
    @Transactional
    public void delete(Long materialId, Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) throw new BusinessException(ResultCode.COMMENT_NOT_FOUND);
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.CANNOT_DELETE_OTHERS_COMMENT);
        }
        // 软删除
        comment.setStatus(0);
        commentMapper.updateById(comment);

        // 减少评论数
        Material material = materialMapper.selectById(materialId);
        if (material != null) {
            Material update = new Material();
            update.setId(materialId);
            update.setCommentCount(Math.max(0, material.getCommentCount() - 1));
            materialMapper.updateById(update);
        }
    }
}
