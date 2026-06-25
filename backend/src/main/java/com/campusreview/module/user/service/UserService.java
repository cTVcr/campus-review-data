package com.campusreview.module.user.service;

import com.campusreview.module.auth.vo.LoginVO;
import com.campusreview.module.user.entity.SysUser;

public interface UserService {
    LoginVO.UserVO getProfile(Long userId);
    void updateProfile(Long userId, SysUser updateInfo);
}
