<script setup lang="ts">
import { useRouter } from 'vue-router'
import { materialApi } from '@/api/material'
import { courseApi } from '@/api/course'
import { MaterialTypeLabels } from '@/types'
import type { Course, MaterialType } from '@/types'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref()
const uploading = ref(false)
const courses = ref<Course[]>([])

const form = reactive({
  title: '',
  description: '',
  courseId: null as number | null,
  type: 'OTHER' as MaterialType,
  year: new Date().getFullYear(),
  semester: '',
})

const fileList = ref<any[]>([])
const file = ref<File | null>(null)

const rules = {
  title: [{ required: true, message: '请输入资料标题', trigger: 'blur' }],
  courseId: [{ required: true, message: '请选择所属课程', trigger: 'change' }],
}

onMounted(async () => {
  try {
    const res = await courseApi.getCourses({ page: 1, size: 100 })
    courses.value = res?.records || []
  } catch { /* */ }
})

function handleFileChange(uploadFile: any) {
  file.value = uploadFile.raw
}

async function handleUpload() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid || !file.value) {
    if (!file.value) ElMessage.warning('请选择文件')
    return
  }
  uploading.value = true
  try {
    await materialApi.upload({
      title: form.title,
      description: form.description,
      courseId: form.courseId!,
      type: form.type,
      year: form.year,
      semester: form.semester || undefined,
      file: file.value,
    })
    ElMessage.success('上传成功！')
    router.push('/user/my-materials')
  } catch { /* handled */ }
  finally { uploading.value = false }
}
</script>

<template>
  <div class="upload-page">
    <h1 class="page-title mb-16">📤 上传复习资料</h1>
    <el-card>
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="资料标题" prop="title">
              <el-input v-model="form.title" placeholder="例如：2023年数据结构期末试卷A卷" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="所属课程" prop="courseId">
              <el-select v-model="form.courseId" placeholder="搜索并选择课程" filterable style="width:100%">
                <el-option v-for="c in courses" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="资料类型">
              <el-select v-model="form.type" style="width:100%">
                <el-option v-for="(label, value) in MaterialTypeLabels" :key="value" :label="label" :value="value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="年份">
              <el-input-number v-model="form.year" :min="2000" :max="2030" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="学期">
              <el-select v-model="form.semester" placeholder="可不选" clearable style="width:100%">
                <el-option label="秋季学期" value="FALL" />
                <el-option label="春季学期" value="SPRING" />
                <el-option label="夏季学期" value="SUMMER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="资料描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="可选：补充说明资料内容..." />
        </el-form-item>

        <el-form-item label="上传文件">
          <el-upload
            v-model:file-list="fileList"
            :auto-upload="false"
            :limit="1"
            drag
            :on-change="handleFileChange"
            accept=".pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.jpg,.jpeg,.png,.md,.txt,.zip"
          >
            <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
            <div class="el-upload__text">拖拽文件到此处或 <em>点击上传</em></div>
            <template #tip>
              <div class="el-upload__tip">支持 PDF、Word、PPT、Excel、图片、Markdown 等格式，最大 50MB</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" @click="handleUpload" :loading="uploading">
            提交上传
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.upload-page {
  max-width: 800px;
  margin: 0 auto;
}
</style>
