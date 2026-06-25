<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { courseApi } from '@/api/course'
import type { Course } from '@/types'

const route = useRoute()
const router = useRouter()
const majorId = Number(route.params.id)
const courses = ref<Course[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await courseApi.getCoursesByMajor(majorId)
    courses.value = res?.data || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <div class="page-header">
      <el-button @click="router.back()" :icon="'ArrowLeft'">返回</el-button>
      <h1 class="page-title mt-16">📖 课程列表</h1>
    </div>
    <el-skeleton :loading="loading" animated :count="3">
      <div class="card-grid">
        <el-card v-for="c in courses" :key="c.id" class="card-item" shadow="hover"
          @click="router.push(`/courses/${c.id}`)">
          <h3>{{ c.name }}</h3>
          <p class="text-secondary" v-if="c.teacher">👨‍🏫 {{ c.teacher }}</p>
          <p class="text-secondary">📄 {{ c.materialCount }} 份资料</p>
        </el-card>
      </div>
    </el-skeleton>
    <el-empty v-if="!loading && courses.length === 0" description="暂无课程数据" />
  </div>
</template>
