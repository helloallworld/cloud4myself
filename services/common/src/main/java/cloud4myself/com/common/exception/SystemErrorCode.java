package cloud4myself.com.common.exception;

import cloud4myself.com.common.exception.errorcode.annotation.ECGetCode;
import cloud4myself.com.common.exception.errorcode.annotation.ECGetHTTPStatus;
import cloud4myself.com.common.exception.errorcode.annotation.ECGetMessage;
import cloud4myself.com.common.exception.errorcode.annotation.ErrorCode;

@ErrorCode("default")
public enum SystemErrorCode {
    SUCCESS(0, 200, "Success"),
    SYSTEM_ERROR(1, 500, "Internal Server Error"),
    FORBIDDEN(2, 403, "Forbidden"),
    UNAUTHORIZED(3, 401, "Unauthorized"),
    NOT_FOUND(4, 404, "Not Found"),
    INVALID_PARAM(5, 400, "Invalid Parameter"),
    LICENSE_FORBIDDEN(6, 403, "License Forbidden"),
    LICENSE_EXPIRED(7, 403, "License Expired"),
    METHOD_NOT_ALLOWED(8, 405, "HTTP Method Not Allowed"),
    MEDIA_NOT_SUPPORTED(9, 415, "Media Type Not Supported"),
    FORWARDING_ERROR(10, 502, "Forwarding Error"),
    GATEWAY_TIMEOUT(11, 504, "Gateway Timeout"),
    ;

    private Integer code;
    private Integer httpCode;
    private String msg;

    SystemErrorCode(Integer code, Integer httpCode, String msg) {
        this.code = code;
        this.httpCode = httpCode;
        this.msg = msg;
    }

    @ECGetCode
    public Integer getCode() {
        return code;
    }

    @ECGetHTTPStatus
    public Integer getHttpCode() {
        return httpCode;
    }

    @ECGetMessage
    public String getMsg() {
        return msg;
    }
}
