package com.campusreview.config.security;

import com.campusreview.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * JWT 认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    private final JwtUtils jwtUtils;

    /** Redis 可选注入，开发环境可能没有 */
    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 检查 Token 黑名单（需 Redis 可用）
            if (redisTemplate != null && Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token))) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtUtils.validateAccessToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = jwtUtils.getUserId(token);
            String username = jwtUtils.getUsername(token);
            String role = jwtUtils.getRole(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            new JwtUserDetails(userId, username, role),
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.debug("JWT 认证失败: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    public void addToBlacklist(String token) {
        if (redisTemplate == null) return;
        long remainingTime = jwtUtils.getRemainingTime(token);
        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + token, "1", remainingTime, TimeUnit.MILLISECONDS);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_NAME);
        if (StringUtils.hasText(header) && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
