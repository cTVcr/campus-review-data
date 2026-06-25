<script setup lang="ts">
import { materialApi } from '@/api/material'
import { MaterialTypeLabels } from '@/types'
import type { Material } from '@/types'

const materials = ref<Material[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await materialApi.getMaterials({ page: 1, size: 50 })
    materials.value = res?.records || []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div>
    <h1 class="page-title mb-16">📁 我的上传</h1>
    <el-table :data="materials" v-loading="loading" stripe>
      <el-table-column prop="title" label="标题" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">{{ MaterialTypeLabels[row.type as keyof typeof MaterialTypeLabels] }}</template>
      </el-table-column>
      <el-table-column prop="courseName" label="课程" width="150" />
      <el-table-column prop="downloadCount" label="下载" width="80" />
      <el-table-column prop="likeCount" label="点赞" width="80" />
      <el-table-column prop="createdAt" label="上传时间" width="160">
        <template #default="{ row }">{{ row.createdAt?.slice(0, 10) }}</template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && materials.length === 0" description="还没有上传过资料" />
  </div>
</template>
