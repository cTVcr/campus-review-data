<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { materialApi } from '@/api/material'
import { MaterialTypeLabels } from '@/types'
import type { Material } from '@/types'
import { ElMessage } from 'element-plus'

const materials = ref<Material[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await materialApi.getMaterials({ page: 1, size: 50 })
    materials.value = res?.records?.filter(m => m.status === 'PENDING') || []
  } finally { loading.value = false }
})
</script>

<template>
  <div>
    <h1 class="page-title mb-16">✅ 资料审核</h1>
    <el-table :data="materials" v-loading="loading" stripe>
      <el-table-column prop="title" label="标题" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">{{ MaterialTypeLabels[row.type as keyof typeof MaterialTypeLabels] }}</template>
      </el-table-column>
      <el-table-column prop="uploaderName" label="上传者" width="120" />
      <el-table-column prop="createdAt" label="上传时间" width="160">
        <template #default="{ row }">{{ row.createdAt?.slice(0, 10) }}</template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && materials.length === 0" description="没有待审核的资料" />
  </div>
</template>
