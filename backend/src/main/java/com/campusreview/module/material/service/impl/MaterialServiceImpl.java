package com.campusreview.module.material.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campusreview.common.constant.ResultCode;
import com.campusreview.common.exception.BusinessException;
import com.campusreview.module.favorite.entity.Favorite;
import com.campusreview.module.favorite.mapper.FavoriteMapper;
import com.campusreview.module.like.entity.MaterialLike;
import com.campusreview.module.like.mapper.MaterialLikeMapper;
import com.campusreview.module.material.entity.Material;
import com.campusreview.module.material.mapper.MaterialMapper;
import com.campusreview.module.material.service.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialMapper materialMapper;
    private final MaterialLikeMapper likeMapper;
    private final FavoriteMapper favoriteMapper;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Override
    public Page<Material> pageQuery(int page, int size, Long courseId, String type,
                                     Integer year, String keyword, String sort, Long currentUserId) {
        Material query = new Material();
        query.setCourseId(courseId);
        query.setType(type);
        query.setYear(year);
        query.setTitle(keyword); // 复用 title 字段传 keyword，在 XML 中处理

        Page<Material> mpPage = new Page<>(page, size);
        List<Material> records = materialMapper.selectMaterialList(mpPage, query);
        mpPage.setRecords(records);

        // 标记当前用户是否已点赞/收藏
        if (currentUserId != null) {
            for (Material m : records) {
                m.setLiked(hasLiked(m.getId(), currentUserId));
                m.setFavorited(hasFavorited(m.getId(), currentUserId));
            }
        }
        return mpPage;
    }

    @Override
    public Material getDetail(Long id, Long currentUserId) {
        Material material = materialMapper.selectById(id);
        if (material == null || !"PUBLISHED".equals(material.getStatus())) {
            throw new BusinessException(ResultCode.MATERIAL_NOT_FOUND);
        }
        // 增加浏览次数
        Material update = new Material();
        update.setId(id);
        update.setViewCount(material.getViewCount() + 1);
        materialMapper.updateById(update);
        material.setViewCount(material.getViewCount() + 1);

        if (currentUserId != null) {
            material.setLiked(hasLiked(id, currentUserId));
            material.setFavorited(hasFavorited(id, currentUserId));
        }
        return material;
    }

    @Override
    @Transactional
    public Material upload(Material material, MultipartFile file, Long uploaderId) {
        // 校验文件
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 保存文件到本地
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String dir = uploadPath + "/" + dateDir;
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";
        String storedName = UUID.randomUUID().toString().replace("-", "") + ext;
        String filePath = dir + "/" + storedName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error("文件保存失败", e);
            throw new BusinessException(ResultCode.MATERIAL_UPLOAD_FAILED);
        }

        material.setUploaderId(uploaderId);
        material.setFileUrl("/files/" + dateDir + "/" + storedName);
        material.setFileName(originalName);
        material.setFileSize(file.getSize());
        material.setFileType(file.getContentType());
        material.setViewCount(0);
        material.setLikeCount(0);
        material.setDownloadCount(0);
        material.setCommentCount(0);
        material.setStatus("PUBLISHED");

        materialMapper.insert(material);
        return material;
    }

    @Override
    public Material update(Long id, Material updateInfo, Long userId) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException(ResultCode.MATERIAL_NOT_FOUND);
        }
        if (!material.getUploaderId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "只能编辑自己上传的资料");
        }
        updateInfo.setId(id);
        materialMapper.updateById(updateInfo);
        return materialMapper.selectById(id);
    }

    @Override
    public void delete(Long id, Long userId) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            throw new BusinessException(ResultCode.MATERIAL_NOT_FOUND);
        }
        if (!material.getUploaderId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "只能删除自己上传的资料");
        }
        materialMapper.deleteById(id);
    }

    @Override
    @Transactional
    public boolean toggleLike(Long materialId, Long userId) {
        Material material = materialMapper.selectById(materialId);
        if (material == null) throw new BusinessException(ResultCode.MATERIAL_NOT_FOUND);

        LambdaQueryWrapper<MaterialLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialLike::getMaterialId, materialId).eq(MaterialLike::getUserId, userId);
        MaterialLike exist = likeMapper.selectOne(wrapper);

        if (exist != null) {
            likeMapper.deleteById(exist.getId());
            material.setLikeCount(Math.max(0, material.getLikeCount() - 1));
            materialMapper.updateById(material);
            return false;
        } else {
            MaterialLike like = new MaterialLike();
            like.setMaterialId(materialId);
            like.setUserId(userId);
            likeMapper.insert(like);
            material.setLikeCount(material.getLikeCount() + 1);
            materialMapper.updateById(material);
            return true;
        }
    }

    @Override
    @Transactional
    public boolean toggleFavorite(Long materialId, Long userId) {
        Material material = materialMapper.selectById(materialId);
        if (material == null) throw new BusinessException(ResultCode.MATERIAL_NOT_FOUND);

        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getMaterialId, materialId).eq(Favorite::getUserId, userId);
        Favorite exist = favoriteMapper.selectOne(wrapper);

        if (exist != null) {
            favoriteMapper.deleteById(exist.getId());
            return false;
        } else {
            Favorite fav = new Favorite();
            fav.setMaterialId(materialId);
            fav.setUserId(userId);
            favoriteMapper.insert(fav);
            return true;
        }
    }

    @Override
    public String getDownloadUrl(Long materialId, Long userId) {
        Material material = materialMapper.selectById(materialId);
        if (material == null) throw new BusinessException(ResultCode.MATERIAL_NOT_FOUND);

        // 增加下载计数
        Material update = new Material();
        update.setId(materialId);
        update.setDownloadCount(material.getDownloadCount() + 1);
        materialMapper.updateById(update);

        return material.getFileUrl();
    }

    private boolean hasLiked(Long materialId, Long userId) {
        return likeMapper.selectCount(
                new LambdaQueryWrapper<MaterialLike>()
                        .eq(MaterialLike::getMaterialId, materialId)
                        .eq(MaterialLike::getUserId, userId)) > 0;
    }

    private boolean hasFavorited(Long materialId, Long userId) {
        return favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getMaterialId, materialId)
                        .eq(Favorite::getUserId, userId)) > 0;
    }
}
