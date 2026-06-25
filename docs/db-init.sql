-- ============================================
-- 校园课程复习资料共享平台 — 数据库初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_review
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE campus_review;

-- ==================== 用户表 ====================
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    email       VARCHAR(100) NOT NULL COMMENT '邮箱',
    password    VARCHAR(255) NOT NULL COMMENT '密码（BCrypt 加密）',
    nickname    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    avatar_url  VARCHAR(255) DEFAULT NULL COMMENT '头像 URL',
    role        VARCHAR(20)  NOT NULL DEFAULT 'STUDENT' COMMENT '角色: STUDENT/TEACHER/ADMIN',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0禁用 1正常',
    provider    VARCHAR(20)  DEFAULT NULL COMMENT 'SSO 来源（预留）',
    open_id     VARCHAR(100) DEFAULT NULL COMMENT 'SSO OpenID（预留）',
    last_login_at DATETIME   DEFAULT NULL COMMENT '最后登录时间',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ==================== 学院表 ====================
CREATE TABLE IF NOT EXISTS college (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL COMMENT '学院名称',
    code        VARCHAR(20)  DEFAULT NULL COMMENT '学院编号',
    sort_order  INT          DEFAULT 0 COMMENT '排序',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学院表';

-- ==================== 专业表 ====================
CREATE TABLE IF NOT EXISTS major (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL COMMENT '专业名称',
    code        VARCHAR(20)  DEFAULT NULL COMMENT '专业编号',
    college_id  BIGINT       NOT NULL COMMENT '所属学院',
    sort_order  INT          DEFAULT 0 COMMENT '排序',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name_college (name, college_id),
    KEY idx_college_id (college_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业表';

-- ==================== 课程表 ====================
CREATE TABLE IF NOT EXISTS course (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(200) NOT NULL COMMENT '课程名称',
    code            VARCHAR(50)  DEFAULT NULL COMMENT '课程编号',
    major_id        BIGINT       NOT NULL COMMENT '所属专业',
    teacher         VARCHAR(100) DEFAULT NULL COMMENT '授课教师',
    credit          DECIMAL(3,1) DEFAULT NULL COMMENT '学分',
    semester        VARCHAR(20)  DEFAULT NULL COMMENT '开课学期',
    description     TEXT         DEFAULT NULL COMMENT '课程简介',
    cover_image     VARCHAR(255) DEFAULT NULL COMMENT '封面图片',
    material_count  INT          DEFAULT 0 COMMENT '资料数量',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_major_id (major_id),
    FULLTEXT INDEX ft_course (name, teacher)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ==================== 资料表 ====================
CREATE TABLE IF NOT EXISTS material (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200) NOT NULL COMMENT '资料标题',
    description     TEXT         DEFAULT NULL COMMENT '资料描述',
    course_id       BIGINT       NOT NULL COMMENT '所属课程',
    type            VARCHAR(20)  NOT NULL DEFAULT 'OTHER' COMMENT '类型: EXAM_PAPER/NOTE/EXERCISE/KEY_POINTS/OTHER',
    file_url        VARCHAR(500) NOT NULL COMMENT '文件访问 URL',
    file_name       VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_size       BIGINT       DEFAULT 0 COMMENT '文件大小（字节）',
    file_type       VARCHAR(50)  DEFAULT NULL COMMENT '文件 MIME 类型',
    page_count      INT          DEFAULT NULL COMMENT '页数（PDF）',
    year            INT          DEFAULT NULL COMMENT '年份',
    semester        VARCHAR(20)  DEFAULT NULL COMMENT '学期: FALL/SPRING/SUMMER',
    uploader_id     BIGINT       NOT NULL COMMENT '上传者',
    view_count      INT          DEFAULT 0 COMMENT '浏览次数',
    like_count      INT          DEFAULT 0 COMMENT '点赞数',
    download_count  INT          DEFAULT 0 COMMENT '下载次数',
    comment_count   INT          DEFAULT 0 COMMENT '评论数',
    status          VARCHAR(20)  NOT NULL DEFAULT 'PUBLISHED' COMMENT '状态: PENDING/PUBLISHED/REJECTED',
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_course_id (course_id),
    KEY idx_type (type),
    KEY idx_year (year),
    KEY idx_uploader (uploader_id),
    KEY idx_status_created (status, created_at),
    FULLTEXT INDEX ft_material (title, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料表';

-- ==================== 评论表 ====================
CREATE TABLE IF NOT EXISTS comment (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT       NOT NULL COMMENT '所属资料',
    user_id     BIGINT       NOT NULL COMMENT '评论者',
    content     TEXT         NOT NULL COMMENT '评论内容',
    parent_id   BIGINT       DEFAULT NULL COMMENT '父评论 ID（嵌套回复）',
    like_count  INT          DEFAULT 0 COMMENT '点赞数',
    status      TINYINT      DEFAULT 1 COMMENT '状态: 0删除 1正常',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_material_id (material_id),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ==================== 点赞表 ====================
CREATE TABLE IF NOT EXISTS material_like (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT   NOT NULL COMMENT '资料 ID',
    user_id     BIGINT   NOT NULL COMMENT '用户 ID',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_material_user (material_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- ==================== 收藏表 ====================
CREATE TABLE IF NOT EXISTS favorite (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT   NOT NULL COMMENT '用户 ID',
    material_id BIGINT   NOT NULL COMMENT '资料 ID',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_material (user_id, material_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ==================== 下载记录表 ====================
CREATE TABLE IF NOT EXISTS download_record (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT       NOT NULL COMMENT '资料 ID',
    user_id     BIGINT       DEFAULT NULL COMMENT '下载者（未登录为 NULL）',
    ip          VARCHAR(50)  DEFAULT NULL COMMENT '下载 IP',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_material_id (material_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下载记录表';

-- ==================== 种子数据：插入默认管理员 ====================
-- 密码: 123456（BCrypt 加密）
INSERT INTO sys_user (username, email, password, nickname, role) VALUES
('admin', 'admin@campus-review.com', '$2a$10$6pSR/awkV9/v4t455ypRxu6oRqKdnsOSLwVPGRahMoRP4R.CFpVJO', '系统管理员', 'ADMIN');
