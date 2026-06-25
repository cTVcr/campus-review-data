import { get, post, put, del } from '@/utils/request'
import type { ApiResult, Material, MaterialQuery, Comment, PageResult, UploadMaterialParams } from '@/types'

export const materialApi = {
  /** 获取资料列表 */
  getMaterials: (params: MaterialQuery) =>
    get<ApiResult<PageResult<Material>>>('/materials', params),

  /** 获取资料详情 */
  getMaterialDetail: (id: number) =>
    get<ApiResult<Material>>(`/materials/${id}`),

  /** 上传资料 */
  upload: (data: UploadMaterialParams) => {
    const formData = new FormData()
    formData.append('file', data.file)
    formData.append('title', data.title)
    if (data.description) formData.append('description', data.description)
    formData.append('courseId', String(data.courseId))
    formData.append('type', data.type)
    if (data.year) formData.append('year', String(data.year))
    if (data.semester) formData.append('semester', data.semester)
    return post<ApiResult<Material>>('/materials', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },

  /** 编辑资料信息 */
  updateMaterial: (id: number, data: Partial<Material>) =>
    put<ApiResult<Material>>(`/materials/${id}`, data),

  /** 删除资料 */
  deleteMaterial: (id: number) =>
    del<ApiResult<null>>(`/materials/${id}`),

  /** 点赞/取消点赞 */
  toggleLike: (id: number) =>
    post<ApiResult<{ liked: boolean }>>(`/materials/${id}/like`),

  /** 收藏/取消收藏 */
  toggleFavorite: (id: number) =>
    post<ApiResult<{ favorited: boolean }>>(`/materials/${id}/favorite`),

  /** 下载资料 */
  download: (id: number) =>
    get<ApiResult<{ url: string }>>(`/materials/${id}/download`),

  /** 获取资料评论 */
  getComments: (materialId: number, params?: { page?: number; size?: number }) =>
    get<ApiResult<PageResult<Comment>>>(`/materials/${materialId}/comments`, params),

  /** 发表评论 */
  addComment: (materialId: number, content: string, parentId?: number) =>
    post<ApiResult<Comment>>(`/materials/${materialId}/comments`, { content, parentId }),

  /** 删除评论 */
  deleteComment: (materialId: number, commentId: number) =>
    del<ApiResult<null>>(`/materials/${materialId}/comments/${commentId}`),
}
