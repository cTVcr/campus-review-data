package com.campusreview.common.exception;

import com.campusreview.common.constant.ResultCode;
import lombok.Getter;

/**
 * 业务异常类
 * <p>
 * 用于在业务逻辑中抛出特定错误，由全局异常处理器统一捕获并返回前端
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 错误码 */
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, String customMessage) {
        super(customMessage);
        this.code = resultCode.getCode();
    }
}
