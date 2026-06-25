<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { collegeApi } from '@/api/college'
import type { Major } from '@/types'

const route = useRoute()
const router = useRouter()
const collegeId = Number(route.params.id)
const majors = ref<Major[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await collegeApi.getMajorsByCollege(collegeId)
    majors.value = res?.data || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <div class="page-header">
      <el-button @click="router.back()" :icon="'ArrowLeft'">返回</el-button>
      <h1 class="page-title mt-16">📚 专业列表</h1>
    </div>
    <el-skeleton :loading="loading" animated :count="3">
      <div class="card-grid">
        <el-card v-for="m in majors" :key="m.id" class="card-item" shadow="hover"
          @click="router.push(`/majors/${m.id}`)">
          <h3>{{ m.name }}</h3>
        </el-card>
      </div>
    </el-skeleton>
    <el-empty v-if="!loading && majors.length === 0" description="暂无专业数据" />
  </div>
</template>
