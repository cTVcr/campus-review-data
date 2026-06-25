import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/HomePage.vue'),
    meta: { title: '首页' },
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/LoginPage.vue'),
    meta: { title: '登录', guest: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/RegisterPage.vue'),
    meta: { title: '注册', guest: true },
  },
  {
    path: '/colleges',
    name: 'Colleges',
    component: () => import('@/views/colleges/CollegeListPage.vue'),
    meta: { title: '学院列表' },
  },
  {
    path: '/colleges/:id',
    name: 'CollegeDetail',
    component: () => import('@/views/colleges/CollegeDetailPage.vue'),
    meta: { title: '学院详情' },
  },
  {
    path: '/majors/:id',
    name: 'MajorDetail',
    component: () => import('@/views/courses/CourseListPage.vue'),
    meta: { title: '课程列表' },
  },
  {
    path: '/courses/:id',
    name: 'CourseDetail',
    component: () => import('@/views/courses/CourseDetailPage.vue'),
    meta: { title: '课程详情' },
  },
  {
    path: '/materials/:id',
    name: 'MaterialDetail',
    component: () => import('@/views/materials/MaterialDetailPage.vue'),
    meta: { title: '资料详情' },
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/search/SearchPage.vue'),
    meta: { title: '搜索' },
  },
  {
    path: '/upload',
    name: 'Upload',
    component: () => import('@/views/upload/UploadPage.vue'),
    meta: { title: '上传资料', requiresAuth: true },
  },
  {
    path: '/user',
    redirect: '/user/profile',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/user/UserProfilePage.vue'),
        meta: { title: '个人中心' },
      },
      {
        path: 'my-materials',
        name: 'MyMaterials',
        component: () => import('@/views/user/MyMaterialsPage.vue'),
        meta: { title: '我的上传' },
      },
      {
        path: 'my-favorites',
        name: 'MyFavorites',
        component: () => import('@/views/user/MyFavoritesPage.vue'),
        meta: { title: '我的收藏' },
      },
    ],
  },
  {
    path: '/admin',
    redirect: '/admin/statistics',
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: 'statistics',
        name: 'AdminStatistics',
        component: () => import('@/views/admin/StatisticsPage.vue'),
        meta: { title: '数据统计' },
      },
      {
        path: 'materials',
        name: 'AdminMaterials',
        component: () => import('@/views/admin/MaterialAuditPage.vue'),
        meta: { title: '资料审核' },
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/UserManagePage.vue'),
        meta: { title: '用户管理' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFoundPage.vue'),
    meta: { title: '404' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title} — 校园复习资料共享平台`

  // 检查是否需要登录
  const token = localStorage.getItem('accessToken')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  next()
})

export default router
