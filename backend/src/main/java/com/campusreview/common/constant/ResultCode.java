package com.campusreview.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一响应状态码
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "服务器内部错误"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或 token 已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "不支持的请求方法"),
    CONFLICT(409, "资源冲突"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后再试"),

    // 业务错误码 1xxx：用户模块
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_EXISTS(1002, "用户名已存在"),
    EMAIL_EXISTS(1003, "邮箱已被注册"),
    PASSWORD_WRONG(1004, "密码错误"),
    USER_DISABLED(1005, "账号已被禁用"),
    VERIFICATION_CODE_ERROR(1006, "验证码错误或已过期"),

    // 业务错误码 2xxx：资料模块
    MATERIAL_NOT_FOUND(2001, "资料不存在"),
    MATERIAL_UPLOAD_FAILED(2002, "资料上传失败"),
    MATERIAL_TYPE_NOT_SUPPORTED(2003, "不支持的文件类型"),
    FILE_TOO_LARGE(2004, "文件大小超出限制"),

    // 业务错误码 3xxx：课程模块
    COLLEGE_NOT_FOUND(3001, "学院不存在"),
    MAJOR_NOT_FOUND(3002, "专业不存在"),
    COURSE_NOT_FOUND(3003, "课程不存在"),

    // 业务错误码 4xxx：互动模块
    ALREADY_LIKED(4001, "已经点过赞了"),
    ALREADY_FAVORITED(4002, "已经收藏过了"),
    COMMENT_NOT_FOUND(4003, "评论不存在"),
    CANNOT_DELETE_OTHERS_COMMENT(4004, "不能删除他人的评论"),
    ;

    private final Integer code;
    private final String message;
}
