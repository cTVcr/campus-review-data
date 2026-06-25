<script setup lang="ts">
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { materialApi } from '@/api/material'
import { MaterialTypeLabels } from '@/types'
import type { Material, Comment as CommentType } from '@/types'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
const materialId = Number(route.params.id)

const material = ref<Material | null>(null)
const comments = ref<CommentType[]>([])
const loading = ref(true)

const commentContent = ref('')
const submitting = ref(false)

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try {
    const [matRes, comRes] = await Promise.all([
      materialApi.getMaterialDetail(materialId),
      materialApi.getComments(materialId),
    ])
    material.value = matRes?.data || null
    comments.value = comRes?.records || []
  } finally {
    loading.value = false
  }
}

async function handleLike() {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    const res = await materialApi.toggleLike(materialId)
    if (material.value) {
      material.value.liked = res.liked
      material.value.likeCount += res.liked ? 1 : -1
    }
  } catch { /* handled */ }
}

async function handleFavorite() {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    const res = await materialApi.toggleFavorite(materialId)
    if (material.value) {
      material.value.favorited = res.favorited
    }
    ElMessage.success(res.favorited ? '已收藏' : '已取消收藏')
  } catch { /* handled */ }
}

async function handleDownload() {
  try {
    const res = await materialApi.download(materialId)
    window.open(res.url, '_blank')
    if (material.value) material.value.downloadCount++
  } catch { /* handled */ }
}

async function handleAddComment() {
  if (!commentContent.value.trim()) return
  submitting.value = true
  try {
    const res = await materialApi.addComment(materialId, commentContent.value)
    comments.value.unshift(res)
    commentContent.value = ''
    ElMessage.success('评论发表成功')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <div>
    <el-skeleton :loading="loading" animated>
      <template v-if="material">
        <div class="page-header">
          <el-tag :type="material.type === 'EXAM_PAPER' ? 'danger' : 'primary'" size="large">
            {{ MaterialTypeLabels[material.type] }}
          </el-tag>
          <h1 class="page-title mt-16">{{ material.title }}</h1>
          <p class="page-subtitle">
            📖 {{ material.courseName }} · 👤 {{ material.uploaderName }}
            · 📅 {{ material.createdAt?.slice(0, 10) }}
            · 📄 {{ material.fileName }}
            <span v-if="material.fileSize">({{ (material.fileSize / 1024 / 1024).toFixed(1) }} MB)</span>
          </p>
        </div>

        <!-- 操作按钮 -->
        <div class="mb-16 flex-between">
          <div style="display:flex;gap:12px">
            <el-button type="primary" @click="handleDownload">
              <el-icon><Download /></el-icon>下载资料
            </el-button>
            <el-button @click="handleLike" :type="material.liked ? 'danger' : 'default'">
              <el-icon><Star /></el-icon>{{ material.liked ? '已点赞' : '点赞' }} ({{ material.likeCount }})
            </el-button>
            <el-button @click="handleFavorite" :type="material.favorited ? 'warning' : 'default'">
              <el-icon><Collection /></el-icon>{{ material.favorited ? '已收藏' : '收藏' }}
            </el-button>
          </div>
          <div class="text-secondary">
            👀 {{ material.viewCount }} · ⬇️ {{ material.downloadCount }}
          </div>
        </div>

        <!-- 描述 -->
        <el-card v-if="material.description" class="mb-16">
          <p>{{ material.description }}</p>
        </el-card>

        <!-- 评论区 -->
        <el-card>
          <template #header><h3>💬 评论 ({{ comments.length }})</h3></template>
          <div v-if="userStore.isLoggedIn" class="mb-16">
            <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..." />
            <el-button type="primary" class="mt-16" @click="handleAddComment" :loading="submitting">
              发表评论
            </el-button>
          </div>
          <el-empty v-if="comments.length === 0" description="暂无评论，来说两句吧" />
          <div v-for="c in comments" :key="c.id" class="comment-item" style="padding:12px 0;border-bottom:1px solid var(--el-border-color-lighter)">
            <div class="flex-between">
              <strong>{{ c.username }}</strong>
              <span class="text-secondary">{{ c.createdAt?.slice(0, 10) }}</span>
            </div>
            <p style="margin-top:8px">{{ c.content }}</p>
          </div>
        </el-card>
      </template>
    </el-skeleton>
  </div>
</template>
