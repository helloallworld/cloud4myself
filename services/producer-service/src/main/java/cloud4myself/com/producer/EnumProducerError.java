package cloud4myself.com.producer;

import cloud4myself.com.common.exception.errorcode.annotation.ECGetCode;
import cloud4myself.com.common.exception.errorcode.annotation.ECGetHTTPStatus;
import cloud4myself.com.common.exception.errorcode.annotation.ECGetMessage;
import cloud4myself.com.common.exception.errorcode.annotation.ErrorCode;

@ErrorCode("PRODUCER-SERVICE")
public enum  EnumProducerError {

    DB_ERROR(9009,666,"调用数据库出现异常");
    private Integer code;
    private Integer httpCode;
    private String message;
    EnumProducerError(Integer code, Integer httpCode, String message) {
        this.code = code;
        this.httpCode = httpCode;
        this.message = message;
    }

    @ECGetCode
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @ECGetHTTPStatus
    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    @ECGetMessage
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
