<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { materialApi } from '@/api/material'
import { MaterialTypeLabels } from '@/types'
import type { Material, MaterialType, Semester } from '@/types'

const route = useRoute()
const router = useRouter()
const keyword = ref((route.query.q as string) || '')
const materials = ref<Material[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const typeFilter = ref<MaterialType | ''>('')

onMounted(() => { if (keyword.value) doSearch() })

function doSearch() {
  if (!keyword.value.trim()) return
  router.replace({ query: { q: keyword.value } })
  loading.value = true
  materialApi.getMaterials({
    keyword: keyword.value,
    type: typeFilter.value || undefined,
    page: page.value,
    size: 12,
  }).then(res => {
    materials.value = res?.records || []
    total.value = res?.total || 0
  }).finally(() => loading.value = false)
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">🔍 搜索结果</h1>
      <div class="mt-16">
        <el-input v-model="keyword" placeholder="搜索资料..." size="large" @keyup.enter="doSearch">
          <template #append><el-button type="primary" @click="doSearch">搜索</el-button></template>
        </el-input>
      </div>
    </div>
    <el-skeleton :loading="loading" animated :count="3">
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
    </el-skeleton>
    <el-empty v-if="!loading && materials.length === 0" description="未找到相关资料" />
  </div>
</template>
