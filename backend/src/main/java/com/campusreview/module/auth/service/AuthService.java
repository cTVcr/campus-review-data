package com.campusreview.module.auth.service;

import com.campusreview.module.auth.dto.LoginDTO;
import com.campusreview.module.auth.dto.RegisterDTO;
import com.campusreview.module.auth.vo.LoginVO;

public interface AuthService {
    void register(RegisterDTO dto);
    LoginVO login(LoginDTO dto);
    LoginVO refreshToken(String refreshToken);
    void logout(String accessToken);
}
