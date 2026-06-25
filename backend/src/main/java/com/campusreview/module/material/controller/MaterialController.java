package com.campusreview.module.material.controller;

import com.campusreview.common.response.Result;
import com.campusreview.common.utils.SecurityUtils;
import com.campusreview.module.material.entity.Material;
import com.campusreview.module.material.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Tag(name = "资料模块")
@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @Operation(summary = "分页查询资料")
    @GetMapping
    public Result<?> listMaterials(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "latest") String sort) {
        Long userId = getCurrentUserIdOrNull();
        return Result.ok(materialService.pageQuery(page, size, courseId, type, year, keyword, sort, userId));
    }

    @Operation(summary = "获取资料详情")
    @GetMapping("/{id}")
    public Result<Material> getDetail(@PathVariable Long id) {
        return Result.ok(materialService.getDetail(id, getCurrentUserIdOrNull()));
    }

    @Operation(summary = "上传资料")
    @PostMapping
    public Result<Material> upload(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("courseId") Long courseId,
            @RequestParam("type") String type,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam("file") MultipartFile file) {
        Material material = new Material();
        material.setTitle(title);
        material.setDescription(description);
        material.setCourseId(courseId);
        material.setType(type);
        material.setYear(year);
        material.setSemester(semester);
        return Result.ok(materialService.upload(material, file, SecurityUtils.getCurrentUserId()));
    }

    @Operation(summary = "编辑资料")
    @PutMapping("/{id}")
    public Result<Material> update(@PathVariable Long id, @RequestBody Material updateInfo) {
        return Result.ok(materialService.update(id, updateInfo, SecurityUtils.getCurrentUserId()));
    }

    @Operation(summary = "删除资料")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        materialService.delete(id, SecurityUtils.getCurrentUserId());
        return Result.ok();
    }

    @Operation(summary = "点赞/取消点赞")
    @PostMapping("/{id}/like")
    public Result<Map<String, Boolean>> toggleLike(@PathVariable Long id) {
        boolean liked = materialService.toggleLike(id, SecurityUtils.getCurrentUserId());
        return Result.ok(Map.of("liked", liked));
    }

    @Operation(summary = "收藏/取消收藏")
    @PostMapping("/{id}/favorite")
    public Result<Map<String, Boolean>> toggleFavorite(@PathVariable Long id) {
        boolean favorited = materialService.toggleFavorite(id, SecurityUtils.getCurrentUserId());
        return Result.ok(Map.of("favorited", favorited));
    }

    @Operation(summary = "下载资料")
    @GetMapping("/{id}/download")
    public Result<Map<String, String>> download(@PathVariable Long id) {
        String url = materialService.getDownloadUrl(id, getCurrentUserIdOrNull());
        return Result.ok(Map.of("url", url));
    }

    /** 获取当前用户ID，未登录返回null */
    private Long getCurrentUserIdOrNull() {
        try {
            return SecurityUtils.getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }
}
