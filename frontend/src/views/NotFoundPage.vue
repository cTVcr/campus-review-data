<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()
</script>

<template>
  <div class="empty-state">
    <h1 style="font-size:72px;color:var(--el-color-primary)">404</h1>
    <p style="font-size:18px;margin:16px 0">页面不存在</p>
    <el-button type="primary" @click="router.push('/')">返回首页</el-button>
  </div>
</template>
