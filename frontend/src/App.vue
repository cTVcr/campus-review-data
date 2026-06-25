<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onMounted(() => {
  // 应用初始化时尝试恢复登录状态
  userStore.restoreLogin()
})
</script>

<template>
  <router-view />
</template>
