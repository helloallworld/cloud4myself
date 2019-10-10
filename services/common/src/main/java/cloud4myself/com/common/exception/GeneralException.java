package cloud4myself.com.common.exception;

import cloud4myself.com.common.exception.errorcode.ErrorCodeFactory;
import cloud4myself.com.common.exception.errorcode.GeneralErrorCode;


public class GeneralException extends RuntimeException {
    private GeneralErrorCode generalErrorCode;
    private String detailMessage;
    private Object data;

    /**
     * 错误提示里面的变量数据
     */
    private Object[] args;

    public GeneralException(Object errorCode) {
        super();
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = this.generalErrorCode.getMessage();
    }

    public GeneralException(Object errorCode, Object[] args) {
        super();
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = this.generalErrorCode.getMessage();
        this.args = args;
    }

    public GeneralException(Object errorCode, Object data) {
        super();
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = this.generalErrorCode.getMessage();
        this.data = data;
    }

    public GeneralException(Object errorCode, Object data, Object[] args) {
        super();
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = this.generalErrorCode.getMessage();
        this.data = data;
        this.args = args;
    }

    public GeneralException(Object errorCode, String message) {
        super(message);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
    }

    public GeneralException(Object errorCode, String message, Object[] args) {
        super(message);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
        this.args = args;
    }

    public GeneralException(Object errorCode, String message, Object data) {
        super(message);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
        this.data = data;
    }

    public GeneralException(Object errorCode, String message, Object data, Object[] args) {
        super(message);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
        this.data = data;
        this.args = args;
    }

    public GeneralException(Object errorCode, String message, Throwable e) {
        super(message, e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
    }

    public GeneralException(Object errorCode, String message, Throwable e, Object[] args) {
        super(message, e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
        this.args = args;
    }

    public GeneralException(Object errorCode, String message, Object data, Throwable e) {
        super(message, e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
        this.data = data;
    }

    public GeneralException(Object errorCode, String message, Object data, Throwable e, Object[] args) {
        super(message, e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, message);
        this.detailMessage = message;
        this.data = data;
        this.args = args;
    }

    public GeneralException(Object errorCode, Throwable e) {
        super(e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = e.getMessage();
    }

    public GeneralException(Object errorCode, Throwable e, Object[] args) {
        super(e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = e.getMessage();
        this.args = args;
    }

    public GeneralException(Object errorCode, Object data, Throwable e) {
        super(e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = e.getMessage();
        this.data = data;
    }

    public GeneralException(Object errorCode, Object data, Throwable e, Object[] args) {
        super(e);
        this.generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        this.detailMessage = e.getMessage();
        this.data = data;
        this.args = args;
    }

    public GeneralErrorCode getGeneralErrorCode() {
        return generalErrorCode;
    }

    public void setGeneralErrorCode(GeneralErrorCode generalErrorCode) {
        this.generalErrorCode = generalErrorCode;
    }

    public String getMessage() {
        return detailMessage;
    }

    public void setMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
