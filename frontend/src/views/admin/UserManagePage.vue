<script setup lang="ts">
import { ref, onMounted } from 'vue'
// 用户管理预留页面
const users: any[] = []
const loading = ref(false)
</script>

<template>
  <div>
    <h1 class="page-title mb-16">👥 用户管理</h1>
    <el-table :data="users" v-loading="loading" stripe>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="role" label="角色" />
      <el-table-column prop="status" label="状态" />
      <el-table-column prop="createdAt" label="注册时间" />
    </el-table>
    <el-empty description="用户管理功能开发中..." />
  </div>
</template>
