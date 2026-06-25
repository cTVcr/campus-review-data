package com.campusreview.module.user.controller;

import com.campusreview.common.response.Result;
import com.campusreview.common.utils.SecurityUtils;
import com.campusreview.module.auth.vo.LoginVO;
import com.campusreview.module.user.entity.SysUser;
import com.campusreview.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户模块")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public Result<LoginVO.UserVO> getProfile() {
        return Result.ok(userService.getProfile(SecurityUtils.getCurrentUserId()));
    }

    @Operation(summary = "修改个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody SysUser updateInfo) {
        userService.updateProfile(SecurityUtils.getCurrentUserId(), updateInfo);
        return Result.ok();
    }
}
