-- ============================================
-- 种子数据：学院、专业、课程
-- 以计算机学院为例，可自行扩展
-- ============================================

USE campus_review;

-- ==================== 学院 ====================
INSERT INTO college (name, code, sort_order) VALUES
('计算机科学与技术学院', 'CS', 1),
('数学与统计学院',       'MATH', 2),
('物理学院',             'PHYS', 3),
('电子信息工程学院',     'EE', 4),
('外国语学院',           'FL', 5),
('经济管理学院',         'EM', 6);

-- ==================== 专业 ====================
-- 计算机学院
INSERT INTO major (name, code, college_id, sort_order) VALUES
('计算机科学与技术', 'CS-CS', 1, 1),
('软件工程',         'CS-SE', 1, 2),
('人工智能',         'CS-AI', 1, 3),
('网络空间安全',     'CS-NS', 1, 4);

-- 数学学院
INSERT INTO major (name, code, college_id, sort_order) VALUES
('数学与应用数学', 'MATH-AM', 2, 1),
('统计学',         'MATH-ST', 2, 2);

-- ==================== 课程 ====================
-- 计算机科学与技术
INSERT INTO course (name, code, major_id, teacher, credit, semester, description) VALUES
('数据结构与算法',    'CS101', 1, '张教授', 4.0, '大二上', '线性表、树、图、排序、查找等经典数据结构与算法'),
('操作系统',          'CS102', 1, '李教授', 4.0, '大二下', '进程管理、内存管理、文件系统、I/O 子系统'),
('计算机网络',        'CS103', 1, '王教授', 3.5, '大二下', 'TCP/IP 协议栈、应用层协议、网络安全基础'),
('计算机组成原理',    'CS104', 1, '赵教授', 4.0, '大二下', 'CPU、存储器、总线、I/O 系统、指令系统'),
('数据库系统概论',    'CS105', 1, '钱教授', 3.0, '大二上', '关系数据库、SQL、范式理论、事务管理');

-- 软件工程
INSERT INTO course (name, code, major_id, teacher, credit, semester, description) VALUES
('软件工程导论',      'SE101', 2, '孙教授', 3.0, '大二上', '软件生命周期、需求分析、设计模式、项目管理'),
('Java 面向对象编程', 'SE102', 2, '周教授', 4.0, '大一下', 'Java 基础、OOP、集合、IO、多线程、反射'),
('Web 前端开发',      'SE103', 2, '吴教授', 3.0, '大二上', 'HTML/CSS/JavaScript、Vue.js、Node.js'),
('软件测试',          'SE104', 2, '郑教授', 3.0, '大三上', '黑盒/白盒测试、单元测试、自动化测试、性能测试');

-- 人工智能
INSERT INTO course (name, code, major_id, teacher, credit, semester, description) VALUES
('机器学习',          'AI101', 3, '冯教授', 4.0, '大二下', '监督学习、无监督学习、深度学习基础'),
('深度学习',          'AI102', 3, '陈教授', 3.5, '大三上', 'CNN、RNN、Transformer、GAN 等现代网络架构'),
('自然语言处理',      'AI103', 3, '褚教授', 3.0, '大三上', '分词、词向量、序列标注、机器翻译、大语言模型');

-- 数学学院
INSERT INTO course (name, code, major_id, teacher, credit, semester, description) VALUES
('高等数学(上)',      'MA101', 5, '卫教授', 5.0, '大一上', '极限、微分、积分、微分方程'),
('高等数学(下)',      'MA102', 5, '卫教授', 5.0, '大一下', '多元微积分、级数、傅里叶分析'),
('线性代数',          'MA103', 5, '蒋教授', 4.0, '大一上', '矩阵、向量空间、特征值、二次型'),
('概率论与数理统计',  'MA201', 6, '沈教授', 4.0, '大一下', '概率空间、随机变量、假设检验、回归分析');

-- ==================== 种子用户 ====================
-- 密码: 123456 (BCrypt加密)
INSERT INTO sys_user (username, email, password, nickname, role) VALUES
('testuser', 'test@campus-review.com', '$2a$10$6pSR/awkV9/v4t455ypRxu6oRqKdnsOSLwVPGRahMoRP4R.CFpVJO', '测试同学', 'STUDENT');
