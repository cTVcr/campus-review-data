# 🎓 校园课程复习资料共享平台

面向全校师生的课程复习资料共享网站 —— 期末复习不用愁！

## 🛠 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + TypeScript + Vite + Element Plus + Pinia |
| 后端 | Spring Boot 3 + MyBatis-Plus + Spring Security + JWT |
| 数据库 | MySQL 8.0 + Redis 7 |
| 部署 | Docker Compose |

## 🚀 快速开始

### 1. 启动基础服务（MySQL + Redis）

```bash
cd docker
docker-compose up -d
```

### 2. 初始化数据库

连接 MySQL 执行 `docs/db-init.sql`

### 3. 启动后端

```bash
cd backend
./mvnw spring-boot:run
```

后端启动后访问 http://localhost:8080/swagger-ui.html 查看 API 文档

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端开发服务器运行在 http://localhost:5173

## 📁 项目结构

```
campus-review/
├── frontend/          # Vue 3 前端
├── backend/           # Spring Boot 3 后端
├── docs/              # 设计文档 + SQL
├── docker/            # Docker Compose
└── README.md
```

## ✨ 核心功能

- 🏫 学院 → 专业 → 课程 三级浏览
- 📤 资料上传（支持 PDF/Word/PPT/图片等）
- 🔍 全文搜索
- 👍 点赞 / ⭐ 收藏 / 💬 评论
- 👤 用户系统（邮箱注册 + JWT 双 Token）
- 📊 管理后台（审核 + 统计）

## 📝 开发进度

- [x] 第一阶段：基础架构（后端骨架 + 前端脚手架 + Docker）
- [ ] 第二阶段：学院课程体系（业务 API + 前端对接）
- [ ] 第三阶段：资料系统（上传/下载/预览）
- [ ] 第四阶段：用户与互动（注册/登录/点赞/收藏/评论）
- [ ] 第五阶段：搜索与管理
- [ ] 第六阶段：优化与文档

## 🤝 贡献

欢迎全校同学参与维护！遇到问题或想添加新功能，欢迎提 Issue。

---

Made with ❤️ by CampusReview Team
