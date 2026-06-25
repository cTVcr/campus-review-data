<script setup lang="ts">
import { useRouter } from 'vue-router'
import { collegeApi } from '@/api/college'
import type { College } from '@/types'

const router = useRouter()
const colleges = ref<College[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await collegeApi.getColleges()
    colleges.value = res?.data || []
  } finally {
    loading.value = false
  }
})

function goToCollege(id: number) {
  router.push(`/colleges/${id}`)
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">🏫 学院列表</h1>
      <p class="page-subtitle">选择学院，浏览各专业课程复习资料</p>
    </div>
    <el-skeleton :loading="loading" animated :count="4">
      <div class="card-grid">
        <el-card v-for="c in colleges" :key="c.id" class="card-item" shadow="hover" @click="goToCollege(c.id)">
          <div style="text-align:center;padding:24px 0">
            <el-icon :size="40" color="var(--el-color-primary)"><School /></el-icon>
            <h3 style="margin-top:12px">{{ c.name }}</h3>
          </div>
        </el-card>
      </div>
    </el-skeleton>
    <el-empty v-if="!loading && colleges.length === 0" description="暂无学院数据" />
  </div>
</template>
