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

  function setTokens(access: string, refresh: string) {
    accessToken.value = access
    refreshToken.value = refresh
    localStorage.setItem('accessToken', access)
    localStorage.setItem('refreshToken', refresh)
  }

  function clearTokens() {
    accessToken.value = ''
    refreshToken.value = ''
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
  }

  /** 登录 —— 响应拦截器已解包，res 直接是 { accessToken, refreshToken, user } */
  async function login(email: string, password: string) {
    const res = await authApi.login({ email, password })
    setTokens(res.accessToken, res.refreshToken)
    user.value = res.user
  }

  async function register(data: { username: string; email: string; password: string }) {
    await authApi.register(data)
  }

  async function logout() {
    try {
      await authApi.logout()
    } finally {
      clearTokens()
      user.value = null
    }
  }

  /** 获取用户信息 —— 响应拦截器已解包 */
  async function fetchProfile() {
    user.value = await authApi.getProfile()
  }

  /** 恢复登录状态 */
  async function restoreLogin() {
    if (!accessToken.value) return
    try {
      await fetchProfile()
    } catch {
      try {
        const res = await authApi.refreshToken()
        setTokens(res.accessToken, res.refreshToken)
        await fetchProfile()
      } catch {
        clearTokens()
      }
    }
  }

  return {
    user, accessToken, refreshToken,
    isLoggedIn, isAdmin,
    login, register, logout,
    fetchProfile, restoreLogin,
    setTokens, clearTokens,
  }
})
