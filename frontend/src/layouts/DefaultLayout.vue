<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

const keyword = ref('')

function handleSearch() {
  if (keyword.value.trim()) {
    router.push({ name: 'Search', query: { q: keyword.value.trim() } })
  }
}

function handleLogout() {
  userStore.logout()
  router.push('/')
}
</script>

<template>
  <el-container class="layout">
    <!-- 顶部导航 -->
    <el-header class="header">
      <div class="header-left">
        <router-link to="/" class="logo">
          <el-icon :size="24"><School /></el-icon>
          <span class="logo-text">校园复习资料</span>
        </router-link>
      </div>

      <div class="header-center">
        <el-input
          v-model="keyword"
          placeholder="搜索课程、资料..."
          prefix-icon="Search"
          class="search-input"
          clearable
          @keyup.enter="handleSearch"
        />
      </div>

      <div class="header-right">
        <template v-if="userStore.isLoggedIn">
          <el-button @click="router.push('/upload')" type="primary" size="small">
            <el-icon><Upload /></el-icon>上传资料
          </el-button>
          <el-dropdown>
            <span class="user-dropdown">
              <el-avatar :size="32" :src="userStore.user?.avatarUrl">
                {{ userStore.user?.nickname?.charAt(0) || userStore.user?.username?.charAt(0) }}
              </el-avatar>
              <span class="username">{{ userStore.user?.nickname || userStore.user?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/user/profile')">
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item @click="router.push('/user/my-materials')">
                  <el-icon><Document /></el-icon>我的上传
                </el-dropdown-item>
                <el-dropdown-item @click="router.push('/user/my-favorites')">
                  <el-icon><Star /></el-icon>我的收藏
                </el-dropdown-item>
                <el-dropdown-item v-if="userStore.isAdmin" @click="router.push('/admin')" divided>
                  <el-icon><Setting /></el-icon>管理后台
                </el-dropdown-item>
                <el-dropdown-item @click="handleLogout" divided>
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button type="primary" @click="router.push('/register')">注册</el-button>
        </template>
      </div>
    </el-header>

    <!-- 主内容区 -->
    <el-main class="main-content">
      <router-view />
    </el-main>

    <!-- 底部 -->
    <el-footer class="footer">
      <p>© 2024 校园复习资料共享平台 — 为全校同学提供优质学习资源</p>
    </el-footer>
  </el-container>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  background: var(--el-bg-color-page);
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid var(--el-border-color-light);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left .logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  color: var(--el-color-primary);
  font-size: 18px;
  font-weight: 700;
}

.header-center {
  flex: 1;
  max-width: 480px;
  margin: 0 40px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  min-height: calc(100vh - 120px);
}

.footer {
  text-align: center;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  padding: 16px;
  border-top: 1px solid var(--el-border-color-light);
}
</style>
