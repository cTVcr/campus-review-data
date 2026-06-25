package com.campusreview.module.material.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusreview.module.material.entity.Material;
import org.springframework.web.multipart.MultipartFile;

public interface MaterialService {
    Page<Material> pageQuery(int page, int size, Long courseId, String type, Integer year, String keyword, String sort, Long currentUserId);
    Material getDetail(Long id, Long currentUserId);
    Material upload(Material material, MultipartFile file, Long uploaderId);
    Material update(Long id, Material updateInfo, Long userId);
    void delete(Long id, Long userId);
    boolean toggleLike(Long materialId, Long userId);
    boolean toggleFavorite(Long materialId, Long userId);
    String getDownloadUrl(Long materialId, Long userId);
}
