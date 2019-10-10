package cloud4myself.com.common.web;

import cloud4myself.com.common.exception.SystemErrorCode;
import cloud4myself.com.common.exception.errorcode.ErrorCodeFactory;
import cloud4myself.com.common.exception.errorcode.GeneralErrorCode;

/**

 描述:
 HttpRequest请求返回的最外层对象,用一种统一的格式返回给前端

 */
public class ResponseMessage<T> {
    //错误码
    private Integer errCode;

    //信息描述
    private String errMsg;

    // 详细错误信息
    private String errDetail;

    //生成消息的服务名
    private String service;

    //具体的信息内容
    private T data;

    private Long requestId = 0L;

    public ResponseMessage(){

    }

    public ResponseMessage(Integer errCode, String errMsg, String service, T data) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.service = service;
        this.data = data;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrDetail() {
        return errDetail;
    }

    public void setErrDetail(String errDetail) {
        this.errDetail = errDetail;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public static ResponseMessage success(Object object, String message) {
        ResponseMessage<Object> responseMessage = new ResponseMessage<>();
        responseMessage.setErrCode(SystemErrorCode.SUCCESS.getCode());
        if(message == null) {
            responseMessage.setErrMsg(SystemErrorCode.SUCCESS.getMsg());
        } else {
            responseMessage.setErrMsg(message);
        }
        responseMessage.setData(object);
        responseMessage.setService("default");
        return responseMessage;
    }

    /**
     * 操作成功不返回消息
     * @return
     */
    public static ResponseMessage success() {
        return success(null, null);
    }

    public static ResponseMessage success(Object obj) {
        return success(obj, null);
    }

    public static ResponseMessage success(String message) {
        return success(null, message);
    }

    /**
     * 操作失败返回的消息
     * @param errorCode
     * @param msg
     * @return
     */
    public static ResponseMessage error(Object errorCode, String msg) {
        ResponseMessage responseMessage = new ResponseMessage();
        GeneralErrorCode generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode, msg);
        responseMessage.setErrCode(generalErrorCode.getCode());
        responseMessage.setErrMsg(generalErrorCode.getMessage());
        responseMessage.setService(generalErrorCode.getService());
        return responseMessage;
    }

    /**
     * 操作失败返回消息，对error的重载
     * @param errorCode
     * @return
     */
    public static ResponseMessage error(Object errorCode){
        ResponseMessage responseMessage = new ResponseMessage();
        GeneralErrorCode generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        responseMessage.setErrCode(generalErrorCode.getCode());
        responseMessage.setErrMsg(generalErrorCode.getMessage());
        responseMessage.setService(generalErrorCode.getService());
        return responseMessage;
    }

    /**
     * 操作失败返回消息，对error的重载
     * @param errorCode
     * @return
     */
    public static ResponseMessage error(Object errorCode, Object data){
        ResponseMessage<Object> responseMessage = new ResponseMessage<>();
        GeneralErrorCode generalErrorCode = ErrorCodeFactory.createErrorCode(errorCode);
        responseMessage.setErrCode(generalErrorCode.getCode());
        responseMessage.setErrMsg(generalErrorCode.getMessage());
        responseMessage.setService(generalErrorCode.getService());
        responseMessage.setData(data);
        return responseMessage;
    }

    public static ResponseMessage error(GeneralErrorCode generalErrorCode) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setErrCode(generalErrorCode.getCode());
        responseMessage.setErrMsg(generalErrorCode.getMessage());
        responseMessage.setService(generalErrorCode.getService());
        return responseMessage;
    }

    public static ResponseMessage error(GeneralErrorCode generalErrorCode, Object data) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setErrCode(generalErrorCode.getCode());
        responseMessage.setErrMsg(generalErrorCode.getMessage());
        responseMessage.setService(generalErrorCode.getService());
        responseMessage.setData(data);
        return responseMessage;
    }

    public static ResponseMessage error(GeneralErrorCode generalErrorCode, String message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setErrCode(generalErrorCode.getCode());
        responseMessage.setErrMsg(message);
        responseMessage.setService(generalErrorCode.getService());
        return responseMessage;
    }

    public static ResponseMessage error(GeneralErrorCode generalErrorCode, String message, Object data) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setErrCode(generalErrorCode.getCode());
        responseMessage.setErrMsg(message);
        responseMessage.setService(generalErrorCode.getService());
        responseMessage.setData(data);
        return responseMessage;
    }
}
