package com.campusreview.module.user.service.impl;

import com.campusreview.common.constant.ResultCode;
import com.campusreview.common.exception.BusinessException;
import com.campusreview.module.auth.vo.LoginVO;
import com.campusreview.module.user.entity.SysUser;
import com.campusreview.module.user.mapper.UserMapper;
import com.campusreview.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public LoginVO.UserVO getProfile(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return LoginVO.UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null)
                .build();
    }

    @Override
    public void updateProfile(Long userId, SysUser updateInfo) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 只允许更新昵称和头像
        SysUser toUpdate = new SysUser();
        toUpdate.setId(userId);
        if (updateInfo.getNickname() != null) toUpdate.setNickname(updateInfo.getNickname());
        if (updateInfo.getAvatarUrl() != null) toUpdate.setAvatarUrl(updateInfo.getAvatarUrl());
        userMapper.updateById(toUpdate);
    }
}
