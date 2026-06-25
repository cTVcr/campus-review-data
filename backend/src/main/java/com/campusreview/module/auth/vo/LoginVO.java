package com.campusreview.module.auth.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {
    private String accessToken;
    private String refreshToken;
    private UserVO user;

    @Data
    @Builder
    public static class UserVO {
        private Long id;
        private String username;
        private String email;
        private String nickname;
        private String avatarUrl;
        private String role;
        private String createdAt;
    }
}
