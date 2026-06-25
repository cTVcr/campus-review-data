package com.campusreview.module.auth.controller;

import com.campusreview.common.response.Result;
import com.campusreview.module.auth.dto.LoginDTO;
import com.campusreview.module.auth.dto.RegisterDTO;
import com.campusreview.module.auth.service.AuthService;
import com.campusreview.module.auth.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "认证模块")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        authService.register(dto);
        return Result.ok();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(authService.login(dto));
    }

    @Operation(summary = "刷新 Token")
    @PostMapping("/refresh-token")
    public Result<LoginVO> refreshToken(@RequestBody Map<String, String> body) {
        return Result.ok(authService.refreshToken(body.get("refreshToken")));
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return Result.ok();
    }
}
