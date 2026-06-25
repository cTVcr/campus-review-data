import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import type { UserInfo } from '@/types'

export const useUserStore = defineStore('user', () => {
  const user = ref<UserInfo | null>(null)
  const accessToken = ref<string>(localStorage.getItem('accessToken') || '')
  const refreshToken = ref<string>(localStorage.getItem('refreshToken') || '')

  const isLoggedIn = computed(() => !!accessToken.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  /** 设置 Token */
  function setTokens(access: string, refresh: string) {
    accessToken.value = access
    refreshToken.value = refresh
    localStorage.setItem('accessToken', access)
    localStorage.setItem('refreshToken', refresh)
  }

  /** 清除 Token */
  function clearTokens() {
    accessToken.value = ''
    refreshToken.value = ''
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
  }

  /** 登录 */
  async function login(email: string, password: string) {
    const res = await authApi.login({ email, password })
    setTokens(res.data.accessToken, res.data.refreshToken)
    user.value = res.data.user
  }

  /** 注册 */
  async function register(data: { username: string; email: string; password: string }) {
    await authApi.register(data)
  }

  /** 登出 */
  async function logout() {
    try {
      await authApi.logout()
    } finally {
      clearTokens()
      user.value = null
    }
  }

  /** 获取用户信息 */
  async function fetchProfile() {
    const res = await authApi.getProfile()
    user.value = res.data
  }

  /** 恢复登录状态（页面刷新时） */
  async function restoreLogin() {
    if (!accessToken.value) return
    try {
      await fetchProfile()
    } catch {
      // Token 过期，尝试刷新
      try {
        const res = await authApi.refreshToken()
        setTokens(res.data.accessToken, res.data.refreshToken)
        await fetchProfile()
      } catch {
        clearTokens()
      }
    }
  }

  return {
    user,
    accessToken,
    refreshToken,
    isLoggedIn,
    isAdmin,
    login,
    register,
    logout,
    fetchProfile,
    restoreLogin,
    setTokens,
    clearTokens,
  }
})
