package com.campusreview.module.favorite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusreview.module.favorite.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
