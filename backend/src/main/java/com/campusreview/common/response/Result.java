package com.campusreview.common.response;

import com.campusreview.common.constant.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果封装
 *
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳 */
    private Long timestamp;

    // ==================== 成功响应 ====================

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), "success", data, System.currentTimeMillis());
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data, System.currentTimeMillis());
    }

    // ==================== 失败响应 ====================

    public static <T> Result<T> fail() {
        return fail(ResultCode.ERROR.getCode(), "操作失败");
    }

    public static <T> Result<T> fail(String message) {
        return fail(ResultCode.ERROR.getCode(), message);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis());
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return fail(resultCode.getCode(), resultCode.getMessage());
    }

    // ==================== 便捷方法 ====================

    public static <T> Result<T> unauthorized() {
        return fail(ResultCode.UNAUTHORIZED);
    }

    public static <T> Result<T> forbidden() {
        return fail(ResultCode.FORBIDDEN);
    }

    public static <T> Result<T> notFound() {
        return fail(ResultCode.NOT_FOUND);
    }

    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(this.code);
    }
}
