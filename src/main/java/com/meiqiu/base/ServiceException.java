package com.meiqiu.base;

/**
 * @Description 封装异常信息
 * @Author sgh
 * @Date 2025/1/17
 * @Time 15:11
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final ResultCode resultCode;

    private final String code;

    public ServiceException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILURE;
        this.code = resultCode.getCode();
    }

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
        this.code = resultCode.getCode();
    }

    public ServiceException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
        this.code = resultCode.getCode();
    }

    public Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }

    public String getCode() {
        return this.getCode();
    }

}
