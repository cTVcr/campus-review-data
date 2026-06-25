package com.campusreview.module.comment.controller;

import com.campusreview.common.response.Result;
import com.campusreview.common.utils.SecurityUtils;
import com.campusreview.module.comment.entity.Comment;
import com.campusreview.module.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "评论模块")
@RestController
@RequestMapping("/api/materials/{materialId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "获取评论列表")
    @GetMapping
    public Result<List<Comment>> list(@PathVariable Long materialId) {
        return Result.ok(commentService.listByMaterialId(materialId));
    }

    @Operation(summary = "发表评论")
    @PostMapping
    public Result<Comment> add(@PathVariable Long materialId, @RequestBody Map<String, Object> body) {
        String content = (String) body.get("content");
        Object parentIdObj = body.get("parentId");
        Long parentId = parentIdObj != null ? ((Number) parentIdObj).longValue() : null;
        return Result.ok(commentService.add(materialId, content, parentId, SecurityUtils.getCurrentUserId()));
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{commentId}")
    public Result<Void> delete(@PathVariable Long materialId, @PathVariable Long commentId) {
        commentService.delete(materialId, commentId, SecurityUtils.getCurrentUserId());
        return Result.ok();
    }
}
