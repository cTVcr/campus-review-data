import { get } from '@/utils/request'
import type { ApiResult, Course, PageResult, PageQuery } from '@/types'

export const courseApi = {
  /** 获取课程列表（支持分页和搜索） */
  getCourses: (params: { majorId?: number; keyword?: string } & PageQuery) =>
    get<ApiResult<PageResult<Course>>>('/courses', params),

  /** 获取某专业下的课程 */
  getCoursesByMajor: (majorId: number) =>
    get<ApiResult<Course[]>>(`/majors/${majorId}/courses`),

  /** 获取课程详情 */
  getCourseDetail: (id: number) =>
    get<ApiResult<Course>>(`/courses/${id}`),
}
