<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { collegeApi } from '@/api/college'
import { materialApi } from '@/api/material'
import type { College, Material } from '@/types'
import { MaterialTypeLabels } from '@/types'

const router = useRouter()

const keyword = ref('')
const colleges = ref<College[]>([])
const hotMaterials = ref<Material[]>([])
const latestMaterials = ref<Material[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    const [collegeRes, hotRes, latestRes] = await Promise.all([
      collegeApi.getColleges(),
      materialApi.getMaterials({ sort: 'popular', page: 1, size: 8 }),
      materialApi.getMaterials({ sort: 'latest', page: 1, size: 8 }),
    ])
    colleges.value = collegeRes?.data || []
    hotMaterials.value = hotRes?.records || []
    latestMaterials.value = latestRes?.records || []
  } catch {
    // 后端未启动时使用模拟数据
  } finally {
    loading.value = false
  }
})

function handleSearch() {
  if (keyword.value.trim()) {
    router.push({ name: 'Search', query: { q: keyword.value.trim() } })
  }
}

function goToCollege(id: number) {
  router.push(`/colleges/${id}`)
}

function goToMaterial(id: number) {
  router.push(`/materials/${id}`)
}
</script>

<template>
  <div class="home-page">
    <!-- 搜索区域 -->
    <section class="hero-section">
      <h1 class="hero-title">📚 校园复习资料共享平台</h1>
      <p class="hero-desc">找到你需要的课程复习资料，或分享你的学习笔记</p>
      <div class="hero-search">
        <el-input
          v-model="keyword"
          placeholder="输入课程名、老师名、资料关键词..."
          size="large"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button type="primary" @click="handleSearch" :icon="'Search'">搜索</el-button>
          </template>
        </el-input>
      </div>
    </section>

    <!-- 学院列表 -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">🏫 按学院浏览</h2>
      </div>
      <el-skeleton :loading="loading" animated :count="3">
        <div class="card-grid">
          <el-card
            v-for="college in colleges"
            :key="college.id"
            class="card-item college-card"
            shadow="hover"
            @click="goToCollege(college.id)"
          >
            <div class="college-card-content">
              <el-icon :size="36" color="var(--el-color-primary)"><School /></el-icon>
              <span class="college-name">{{ college.name }}</span>
            </div>
          </el-card>
        </div>
      </el-skeleton>
      <el-empty v-if="!loading && colleges.length === 0" description="暂无学院数据" />
    </section>

    <!-- 热门资料 -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">🔥 热门资料</h2>
      </div>
      <el-skeleton :loading="loading" animated :count="3">
        <div class="card-grid">
          <el-card
            v-for="item in hotMaterials"
            :key="item.id"
            class="card-item"
            shadow="hover"
            @click="goToMaterial(item.id)"
          >
            <template #header>
              <div class="material-card-header">
                <el-tag size="small" :type="item.type === 'EXAM_PAPER' ? 'danger' : 'primary'">
                  {{ MaterialTypeLabels[item.type] }}
                </el-tag>
                <span class="material-year" v-if="item.year">{{ item.year }}</span>
              </div>
            </template>
            <h3 class="material-title">{{ item.title }}</h3>
            <p class="material-course">{{ item.courseName }}</p>
            <div class="material-stats">
              <span><el-icon><View /></el-icon> {{ item.viewCount }}</span>
              <span><el-icon><Download /></el-icon> {{ item.downloadCount }}</span>
              <span><el-icon><Star /></el-icon> {{ item.likeCount }}</span>
            </div>
          </el-card>
        </div>
      </el-skeleton>
      <el-empty v-if="!loading && hotMaterials.length === 0" description="暂无资料，快来上传第一份吧！" />
    </section>

    <!-- 最新上传 -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">🆕 最新上传</h2>
      </div>
      <el-skeleton :loading="loading" animated :count="3">
        <div class="card-grid">
          <el-card
            v-for="item in latestMaterials"
            :key="item.id"
            class="card-item"
            shadow="hover"
            @click="goToMaterial(item.id)"
          >
            <template #header>
              <div class="material-card-header">
                <el-tag size="small">{{ MaterialTypeLabels[item.type] }}</el-tag>
                <span class="material-uploader text-secondary">{{ item.uploaderName }}</span>
              </div>
            </template>
            <h3 class="material-title">{{ item.title }}</h3>
            <p class="material-course">{{ item.courseName }}</p>
            <p class="material-date text-secondary">{{ item.createdAt?.slice(0, 10) }}</p>
          </el-card>
        </div>
      </el-skeleton>
    </section>
  </div>
</template>

<style scoped>
.hero-section {
  text-align: center;
  padding: 48px 20px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  color: #fff;
  margin-bottom: 40px;
}

.hero-title {
  font-size: 32px;
  margin-bottom: 12px;
}

.hero-desc {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 28px;
}

.hero-search {
  max-width: 600px;
  margin: 0 auto;
}

.section {
  margin-bottom: 40px;
}

.section-header {
  margin-bottom: 20px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
}

.college-card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
}

.college-name {
  font-size: 15px;
  font-weight: 500;
}

.material-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.material-title {
  font-size: 15px;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.material-course {
  color: var(--el-text-color-secondary);
  font-size: 13px;
  margin-bottom: 8px;
}

.material-stats {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.material-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
