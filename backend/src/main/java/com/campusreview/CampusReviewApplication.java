package com.campusreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园课程复习资料共享平台 — 启动类
 *
 * @author CampusReview Team
 */
@SpringBootApplication
public class CampusReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusReviewApplication.class, args);
        System.out.println("""

                ============================================
                  🎓 Campus Review 启动成功！
                  📖 API 文档: http://localhost:8080/swagger-ui.html
                  🔍 API JSON: http://localhost:8080/v3/api-docs
                ============================================
                """);
    }
}
