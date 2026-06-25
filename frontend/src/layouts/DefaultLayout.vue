<script setup lang="ts">
import { ref } from 'vue'
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
  <div class="app-layout">
    <header class="app-header">
      <div class="header-left">
        <router-link to="/" class="logo">
          🎓 <span>校园复习资料</span>
        </router-link>
      </div>
      <div class="header-center">
        <el-input
          v-model="keyword"
          placeholder="搜索课程、资料..."
          size="default"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
      </div>
      <div class="header-right">
        <template v-if="userStore.isLoggedIn">
          <el-button @click="router.push('/upload')" type="primary" size="small">
            上传资料
          </el-button>
          <el-dropdown>
            <span class="user-dropdown">
              <el-avatar :size="30">{{ userStore.user?.nickname?.charAt(0) || 'U' }}</el-avatar>
              <span>{{ userStore.user?.nickname }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/user/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="router.push('/user/my-materials')">我的上传</el-dropdown-item>
                <el-dropdown-item @click="router.push('/user/my-favorites')">我的收藏</el-dropdown-item>
                <el-dropdown-item v-if="userStore.isAdmin" @click="router.push('/admin')" divided>管理后台</el-dropdown-item>
                <el-dropdown-item @click="handleLogout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button type="primary" @click="router.push('/register')">注册</el-button>
        </template>
      </div>
    </header>

    <main class="app-main">
      <router-view />
    </main>

    <footer class="app-footer">
      <p>© 2024 校园复习资料共享平台 — 为全校同学提供优质学习资源</p>
    </footer>
  </div>
</template>

<style>
/* 不 scoped，确保布局正常工作 */
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left .logo {
  display: flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  color: #409eff;
  font-size: 18px;
  font-weight: 700;
}

.header-center {
  flex: 1;
  max-width: 460px;
  margin: 0 32px;
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

.app-main {
  flex: 1;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 16px;
}

.app-footer {
  text-align: center;
  color: #909399;
  font-size: 13px;
  padding: 16px;
  border-top: 1px solid #ebeef5;
  background: #fff;
}
</style>
