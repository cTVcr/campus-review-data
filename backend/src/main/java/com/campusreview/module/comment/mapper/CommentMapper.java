package com.campusreview.module.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusreview.module.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*, u.nickname AS username, u.avatar_url AS avatarUrl " +
            "FROM comment c LEFT JOIN sys_user u ON c.user_id = u.id " +
            "WHERE c.material_id = #{materialId} AND c.status = 1 AND c.parent_id IS NULL " +
            "ORDER BY c.created_at DESC")
    List<Comment> selectByMaterialId(Long materialId);

    @Select("SELECT c.*, u.nickname AS username, u.avatar_url AS avatarUrl " +
            "FROM comment c LEFT JOIN sys_user u ON c.user_id = u.id " +
            "WHERE c.parent_id = #{parentId} AND c.status = 1 " +
            "ORDER BY c.created_at ASC")
    List<Comment> selectReplies(Long parentId);
}
