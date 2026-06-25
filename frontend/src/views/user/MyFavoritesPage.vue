<script setup lang="ts">
import { useRouter } from 'vue-router'
import { materialApi } from '@/api/material'
import { MaterialTypeLabels } from '@/types'
import type { Material } from '@/types'

const router = useRouter()
const materials = ref<Material[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await materialApi.getMaterials({ page: 1, size: 50 })
    materials.value = res?.records || []
  } finally { loading.value = false }
})
</script>

<template>
  <div>
    <h1 class="page-title mb-16">⭐ 我的收藏</h1>
    <div class="card-grid">
      <el-card v-for="m in materials" :key="m.id" class="card-item" shadow="hover"
        @click="router.push(`/materials/${m.id}`)">
        <template #header>
          <el-tag size="small">{{ MaterialTypeLabels[m.type] }}</el-tag>
        </template>
        <h3>{{ m.title }}</h3>
        <p class="text-secondary">{{ m.courseName }}</p>
      </el-card>
    </div>
    <el-empty v-if="!loading && materials.length === 0" description="还没有收藏过资料" />
  </div>
</template>
