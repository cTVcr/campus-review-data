import { get } from '@/utils/request'
import type { ApiResult, College, Major } from '@/types'

export const collegeApi = {
  /** 获取所有学院 */
  getColleges: () =>
    get<ApiResult<College[]>>('/colleges'),

  /** 获取学院下的专业列表 */
  getMajorsByCollege: (collegeId: number) =>
    get<ApiResult<Major[]>>(`/colleges/${collegeId}/majors`),
}
