// ==================== API 响应 ====================
export interface ApiResult<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}

// ==================== 分页 ====================
export interface PageQuery {
  page: number
  size: number
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

// ==================== 用户 ====================
export interface UserInfo {
  id: number
  username: string
  email: string
  nickname?: string
  avatarUrl?: string
  role: 'STUDENT' | 'TEACHER' | 'ADMIN'
  status: number
  createdAt: string
}

export interface LoginParams {
  email: string
  password: string
}

export interface RegisterParams {
  username: string
  email: string
  password: string
}

export interface LoginResult {
  accessToken: string
  refreshToken: string
  user: UserInfo
}

// ==================== 学院/专业/课程 ====================
export interface College {
  id: number
  name: string
  code?: string
  sortOrder: number
}

export interface Major {
  id: number
  name: string
  code?: string
  collegeId: number
  sortOrder: number
}

export interface Course {
  id: number
  name: string
  code?: string
  majorId: number
  teacher?: string
  credit?: number
  semester?: string
  description?: string
  coverImage?: string
  materialCount: number
  // 联表查询字段
  majorName?: string
  collegeName?: string
}

// ==================== 资料 ====================
export type MaterialType = 'EXAM_PAPER' | 'NOTE' | 'EXERCISE' | 'KEY_POINTS' | 'OTHER'
export type MaterialStatus = 'PENDING' | 'PUBLISHED' | 'REJECTED'
export type Semester = 'FALL' | 'SPRING' | 'SUMMER'

export const MaterialTypeLabels: Record<MaterialType, string> = {
  EXAM_PAPER: '期末试卷',
  NOTE: '复习笔记',
  EXERCISE: '课后习题',
  KEY_POINTS: '重点考点',
  OTHER: '其他资料',
}

export const SemesterLabels: Record<Semester, string> = {
  FALL: '秋季学期',
  SPRING: '春季学期',
  SUMMER: '夏季学期',
}

export interface Material {
  id: number
  title: string
  description?: string
  courseId: number
  type: MaterialType
  fileUrl: string
  fileName: string
  fileSize: number
  fileType?: string
  pageCount?: number
  year?: number
  semester?: Semester
  uploaderId: number
  viewCount: number
  likeCount: number
  downloadCount: number
  commentCount: number
  status: MaterialStatus
  createdAt: string
  // 联表查询
  uploaderName?: string
  courseName?: string
  liked?: boolean
  favorited?: boolean
}

export interface MaterialQuery {
  courseId?: number
  type?: MaterialType
  year?: number
  semester?: Semester
  keyword?: string
  sort?: 'latest' | 'popular' | 'downloads'
  page?: number
  size?: number
}

// ==================== 评论 ====================
export interface Comment {
  id: number
  materialId: number
  userId: number
  content: string
  parentId?: number
  likeCount: number
  createdAt: string
  // 联表
  username?: string
  avatarUrl?: string
  replies?: Comment[]
}

// ==================== 文件上传 ====================
export interface UploadMaterialParams {
  title: string
  description?: string
  courseId: number
  type: MaterialType
  year?: number
  semester?: Semester
  file: File
}
