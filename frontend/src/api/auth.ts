import { post, get, put } from '@/utils/request'
import type { LoginParams, LoginResult, RegisterParams, UserInfo, ApiResult } from '@/types'

export const authApi = {
  /** 邮箱注册 */
  register: (data: RegisterParams) =>
    post<ApiResult<null>>('/auth/register', data),

  /** 邮箱 + 密码登录 */
  login: (data: LoginParams) =>
    post<ApiResult<LoginResult>>('/auth/login', data),

  /** 刷新 Token */
  refreshToken: () =>
    post<ApiResult<{ accessToken: string; refreshToken: string }>>(
      '/auth/refresh-token',
      { refreshToken: localStorage.getItem('refreshToken') }
    ),

  /** 登出 */
  logout: () => post<ApiResult<null>>('/auth/logout'),

  /** 发送邮箱验证码 */
  sendCode: (email: string) =>
    get<ApiResult<null>>('/auth/send-code', { email }),

  /** 获取当前用户信息 */
  getProfile: () =>
    get<ApiResult<UserInfo>>('/user/profile'),

  /** 修改个人信息 */
  updateProfile: (data: Partial<UserInfo>) =>
    put<ApiResult<UserInfo>>('/user/profile', data),
}
