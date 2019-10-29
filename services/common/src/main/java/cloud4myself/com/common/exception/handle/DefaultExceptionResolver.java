//package cloud4myself.com.common.exception.handle;
//
//import cloud4myself.com.common.exception.GeneralException;
//import cloud4myself.com.common.exception.SystemErrorCode;
//import cloud4myself.com.common.web.ResponseMessage;
//import com.netflix.hystrix.exception.HystrixBadRequestException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import javax.ws.rs.WebApplicationException;
//import java.util.Locale;
//import java.util.Set;
//
//@ControllerAdvice
//public class DefaultExceptionResolver extends ExceptionHandlerExceptionResolver {
//    private final static Logger logger = LoggerFactory.getLogger(DefaultExceptionResolver.class);
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    private HttpServletResponse response;
//
//    @Autowired
//    private MessageSource messageSource;
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public ResponseMessage handleException(Exception e){
//
//        logger.error("[业务异常] {}", e);
//        ResponseMessage ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, "");
//        if (e instanceof GeneralException) {//自定义异常GeneralException
//            ret = handleGeneralException((GeneralException)e);
//        } else if (e instanceof WebApplicationException) {//web应用异常WebApplicationException
//            WebApplicationException webex = (WebApplicationException) e;
//            response.setStatus(webex.getResponse().getStatus());
//            try {
//                ret = (ResponseMessage) webex.getResponse().getEntity();
//            } catch (Exception e1) {
//                logger.error("{}", e);
//                ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
//            }
//        } else if (e instanceof HystrixBadRequestException) {//Hystrix异常HystrixBadRequestException
//            Throwable throwable = e.getCause();
//            if (throwable == null) {
//                ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
//            } else {
//                logger.error("Feign execution error, ", throwable);
//                if (throwable instanceof WebApplicationException) {
//                    WebApplicationException ex = (WebApplicationException) throwable;
//                    response.setStatus(ex.getResponse().getStatus());
//                    Object entity = ex.getResponse().getEntity();
//                    if (entity == null) {
//                        ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, ex.getMessage());
//                    } else {
//                        if (entity instanceof ResponseMessage) {
//                            ret = (ResponseMessage) entity;
//                        } else {
//                            ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, ex.getMessage());
//                        }
//                    }
//                } else {
//                    response.setStatus(SystemErrorCode.SYSTEM_ERROR.getHttpCode());
//                    ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, throwable.getMessage());
//                }
//            }
//        } else if (e instanceof ConstraintViolationException) {//参数校验异常ConstraintViolationException
//            ret = handleConstraintViolationException((ConstraintViolationException) e);
//        } else {//其他异常视为系统异常
//            ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
//        }
//
//        // 忽略ErrDetail，否则会暴露系统内部数据
//        logger.error("Error Detail: " + ret.getErrDetail());
//        ret.setErrDetail(null);
//
//        return ret;
//
//    }
//    private ResponseMessage handleConstraintViolationException(ConstraintViolationException e) {
//        response.setStatus(SystemErrorCode.INVALID_PARAM.getHttpCode());
//        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
//        StringBuilder sb = new StringBuilder();
//        int i = 0;
//        int size = constraintViolations.size();
//        for (ConstraintViolation constraintViolation : constraintViolations) {
//            sb.append(constraintViolation.getPropertyPath()).append(": ");
//            String messageKey = constraintViolation.getMessageTemplate();
//            String message = getLocaleMessage(messageKey, null);
//            if (message == null) {
//                //默认国际化消息
//                message = constraintViolation.getMessage();
//            }
//            sb.append(message);
//            if (size > 1 && i < size - 1) {
//                sb.append(", ");
//            }
//            i++;
//        }
//        ResponseMessage responseMessage = ResponseMessage.error(SystemErrorCode.INVALID_PARAM, sb.toString());
//        responseMessage.setErrDetail(e.getMessage());
//        return responseMessage;
//    }
//    private ResponseMessage handleSystemErrorCode(SystemErrorCode systemErrorCode, String errDetail) {
//        response.setStatus(systemErrorCode.getHttpCode());
//        String messageKey = "default_" + systemErrorCode.name();
//        String message = getLocaleMessage(messageKey, null);
//        if(message == null) {
//            message = systemErrorCode.getMsg();
//        }
//        ResponseMessage responseMessage = ResponseMessage.error(systemErrorCode, message);
//        responseMessage.setErrDetail(errDetail);
//        return responseMessage;
//    }
//    private ResponseMessage handleGeneralException(GeneralException e) {
//        response.setStatus(e.getGeneralErrorCode().getHttpCode());
//        String errMessage = getLocaleMessage(e);
//        ResponseMessage responseMessage = ResponseMessage.error(e.getGeneralErrorCode(), errMessage);
//        responseMessage.setData(e.getData());
//        responseMessage.setErrDetail(e.getMessage());
//        return responseMessage;
//    }
//    private String getLocaleMessage(GeneralException e) {
//        String messageKey = e.getGeneralErrorCode().getService().toUpperCase() + "_"
//                + e.getGeneralErrorCode().getStrCode().toUpperCase();
//        String msg = getLocaleMessage(messageKey, e.getArgs());
//        if(msg == null) {
//            return e.getMessage();
//        }
//        return msg;
//    }
//    /**
//     * 获取本地化消息
//     *
//     * @param messageKey
//     * @param args
//     * @return
//     */
//    private String getLocaleMessage(String messageKey, Object[] args) {
//        String localMessage = null;
//        try {
//            logger.info("messageKey: {}", messageKey);
//            Locale locale = LocaleContextHolder.getLocale();
//            localMessage = messageSource.getMessage(messageKey, args, null, locale);
//            logger.info("localMessage: {}", localMessage);
//        } catch (Exception e) {
//            logger.error("get local message error! {}", e);
//        }
//        return localMessage;
//    }
//}
