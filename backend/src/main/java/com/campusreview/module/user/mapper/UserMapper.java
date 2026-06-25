package com.campusreview.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusreview.module.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    @Select("SELECT * FROM sys_user WHERE username = #{username} AND status = 1")
    SysUser selectByUsername(String username);

    @Select("SELECT * FROM sys_user WHERE email = #{email} AND status = 1")
    SysUser selectByEmail(String email);
}
