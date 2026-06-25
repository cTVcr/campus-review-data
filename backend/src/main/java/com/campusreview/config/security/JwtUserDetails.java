package com.campusreview.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * JWT 中解析出的用户信息，存储在 SecurityContext 中
 */
@Getter
@AllArgsConstructor
public class JwtUserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final String username;
    private final String role;
}
