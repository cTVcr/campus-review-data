<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<template>
  <div>
    <h1 class="page-title mb-16">👤 个人中心</h1>
    <el-card v-if="userStore.user">
      <el-descriptions title="用户信息" :column="2" border>
        <el-descriptions-item label="用户名">{{ userStore.user.username }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userStore.user.email }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ userStore.user.nickname || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ userStore.user.role === 'ADMIN' ? '管理员' : userStore.user.role === 'TEACHER' ? '教师' : '学生' }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ userStore.user.createdAt?.slice(0, 10) }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>
