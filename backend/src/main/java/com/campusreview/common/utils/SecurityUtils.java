package com.campusreview.common.utils;

import com.campusreview.common.exception.BusinessException;
import com.campusreview.config.security.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.campusreview.common.constant.ResultCode.UNAUTHORIZED;

/**
 * 安全工具类 —— 从 SecurityContext 获取当前登录用户信息
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前登录用户信息
     */
    public static JwtUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtUserDetails) {
            return (JwtUserDetails) principal;
        }
        throw new BusinessException(UNAUTHORIZED);
    }

    /**
     * 获取当前用户 ID
     */
    public static Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * 判断当前用户是否为管理员
     */
    public static boolean isAdmin() {
        return "ADMIN".equals(getCurrentUser().getRole());
    }
}
