<script setup lang="ts">
import { ref, onMounted } from 'vue'
const stats = [
  { label: '用户总数', value: '--', icon: 'User' },
  { label: '资料总数', value: '--', icon: 'Document' },
  { label: '下载总量', value: '--', icon: 'Download' },
  { label: '今日活跃', value: '--', icon: 'TrendCharts' },
]
</script>

<template>
  <div>
    <h1 class="page-title mb-16">📊 数据统计</h1>
    <el-row :gutter="20">
      <el-col :span="6" v-for="s in stats" :key="s.label">
        <el-card shadow="hover">
          <div style="text-align:center">
            <h2>{{ s.value }}</h2>
            <p class="text-secondary">{{ s.label }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
