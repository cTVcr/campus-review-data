<script setup lang="ts">
import { useRoute } from 'vue-router'
import { courseApi } from '@/api/course'
import { materialApi } from '@/api/material'
import type { Course, Material, MaterialType } from '@/types'
import { MaterialTypeLabels } from '@/types'

const route = useRoute()
const courseId = Number(route.params.id)
const course = ref<Course | null>(null)
const materials = ref<Material[]>([])
const total = ref(0)
const page = ref(1)
const typeFilter = ref<MaterialType | ''>('')
const loading = ref(true)

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try {
    const [courseRes, matRes] = await Promise.all([
      courseApi.getCourseDetail(courseId),
      materialApi.getMaterials({
        courseId,
        type: typeFilter.value || undefined,
        page: page.value,
        size: 12,
      }),
    ])
    course.value = courseRes?.data || null
    materials.value = matRes?.records || []
    total.value = matRes?.total || 0
  } finally {
    loading.value = false
  }
}

function onTypeChange() {
  page.value = 1
  fetchData()
}

function onPageChange(p: number) {
  page.value = p
  fetchData()
}

const typeOptions: { label: string; value: MaterialType }[] = Object.entries(MaterialTypeLabels).map(
  ([value, label]) => ({ label, value: value as MaterialType })
)
</script>

<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">📖 {{ course?.name || '加载中...' }}</h1>
      <p class="page-subtitle" v-if="course">
        👨‍🏫 {{ course.teacher || '未知教师' }} · 📄 {{ course.materialCount }} 份资料
        <span v-if="course.description"><br />{{ course.description }}</span>
      </p>
    </div>

    <!-- 类型筛选 -->
    <div class="mb-16">
      <el-radio-group v-model="typeFilter" @change="onTypeChange">
        <el-radio-button value="">全部</el-radio-button>
        <el-radio-button v-for="opt in typeOptions" :key="opt.value" :value="opt.value">
          {{ opt.label }}
        </el-radio-button>
      </el-radio-group>
    </div>

    <!-- 资料列表 -->
    <el-skeleton :loading="loading" animated :count="3">
      <div class="card-grid">
        <el-card v-for="m in materials" :key="m.id" class="card-item" shadow="hover"
          @click="$router.push(`/materials/${m.id}`)">
          <template #header>
            <div class="flex-between">
              <el-tag size="small" :type="m.type === 'EXAM_PAPER' ? 'danger' : 'primary'">
                {{ MaterialTypeLabels[m.type] }}
              </el-tag>
              <span class="text-secondary" v-if="m.year">{{ m.year }}</span>
            </div>
          </template>
          <h3>{{ m.title }}</h3>
          <div class="material-stats mt-16">
            <span>👀 {{ m.viewCount }}</span>
            <span>⬇️ {{ m.downloadCount }}</span>
            <span>👍 {{ m.likeCount }}</span>
          </div>
        </el-card>
      </div>
    </el-skeleton>

    <el-empty v-if="!loading && materials.length === 0" description="暂无资料" />

    <!-- 分页 -->
    <div class="text-center mt-16" v-if="total > 12">
      <el-pagination
        v-model:current-page="page"
        :total="total"
        :page-size="12"
        layout="prev, pager, next"
        @change="onPageChange"
      />
    </div>
  </div>
</template>
