package cloud4myself.com.common.exception.handle;


import cloud4myself.com.common.exception.GeneralException;
import cloud4myself.com.common.exception.SystemErrorCode;
import cloud4myself.com.common.web.ResponseMessage;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 异常统一处理助手：
 * (1)统一处理系统所有异常
 * (2)统一处理异常国际化
 * (3)各个业务模块尽量只抛出两类异常：自定义异常GeneralException、参数校验异常ConstraintViolationException
 *
 * 注意：此类写在common包中，其他服务引用是，需要在启动类上加 @ComponentScan(basePackages = "cloud4myself.com.*")
 * 才能扫描到，否则扫描不到该类，本人智障操作，让自己疑惑了很久。。。。
 *
 * 该包下的DefaultExceptionResolver类也是一样的作用
 *
 */
@ControllerAdvice
@RestController(value = "/error")
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private HttpServletResponse response;

    @Autowired
    private MessageSource messageSource;

    /**
     * 参数校验消息体异常处理国际化
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String messageKey;
        String errMessage;

        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        int size = objectErrors.size();
        //消息体中参数可能有多个，逐个处理异常国际化
        for (int i = 0; i < size; i++) {
            ObjectError objectError = objectErrors.get(i);
            messageKey = objectError.getDefaultMessage();
            errMessage = getLocaleMessage(messageKey, null);
            if (errMessage == null) {
                errMessage = messageKey;
                logger.debug("default errMessage: {}", errMessage);
            }
            sb.append(errMessage);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        logger.debug("errMessage: {}", sb.toString());

        ResponseMessage responseMessage = ResponseMessage.error(SystemErrorCode.INVALID_PARAM, sb.toString());
        responseMessage.setErrDetail(ex.getMessage());

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseMessage responseMessage = handleSystemErrorCode(SystemErrorCode.METHOD_NOT_ALLOWED, ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseMessage responseMessage = handleSystemErrorCode(SystemErrorCode.MEDIA_NOT_SUPPORTED, ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseMessage responseMessage = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseMessage responseMessage = handleSystemErrorCode(SystemErrorCode.INVALID_PARAM, ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseMessage responseMessage = handleSystemErrorCode(SystemErrorCode.INVALID_PARAM, ex.getMessage());
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * 异常统一处理国际化：
     * (1)自定义异常GeneralException
     * (2)web应用异常WebApplicationException
     * (3)Hystrix异常HystrixBadRequestException
     * (4)参数校验异常ConstraintViolationException
     * (5)其他异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseMessage handle(Exception e) {
        logger.error("[业务异常] {}", e);
        ResponseMessage ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, "");
        if (e instanceof GeneralException) {//自定义异常GeneralException
            ret = handleGeneralException((GeneralException)e);
        } else if (e instanceof WebApplicationException) {//web应用异常WebApplicationException
            WebApplicationException webex = (WebApplicationException) e;
            response.setStatus(webex.getResponse().getStatus());
            try {
                ret = (ResponseMessage) webex.getResponse().getEntity();
            } catch (Exception e1) {
                logger.error("{}", e);
                ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
            }
        } else if (e instanceof HystrixBadRequestException) {//Hystrix异常HystrixBadRequestException
            Throwable throwable = e.getCause();
            if (throwable == null) {
                ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
            } else {
                logger.error("Feign execution error, ", throwable);
                if (throwable instanceof WebApplicationException) {
                    WebApplicationException ex = (WebApplicationException) throwable;
                    response.setStatus(ex.getResponse().getStatus());
                    Object entity = ex.getResponse().getEntity();
                    if (entity == null) {
                        ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, ex.getMessage());
                    } else {
                        if (entity instanceof ResponseMessage) {
                            ret = (ResponseMessage) entity;
                        } else {
                            ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, ex.getMessage());
                        }
                    }
                } else {
                    response.setStatus(SystemErrorCode.SYSTEM_ERROR.getHttpCode());
                    ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, throwable.getMessage());
                }
            }
        } else if (e instanceof ConstraintViolationException) {//参数校验异常ConstraintViolationException
            ret = handleConstraintViolationException((ConstraintViolationException) e);
        } else {//其他异常视为系统异常
            ret = handleSystemErrorCode(SystemErrorCode.SYSTEM_ERROR, e.getMessage());
        }

        // 忽略ErrDetail，否则会暴露系统内部数据
        logger.error("Error Detail: " + ret.getErrDetail());
        ret.setErrDetail(null);

        return ret;
    }

    private ResponseMessage handleConstraintViolationException(ConstraintViolationException e) {
        response.setStatus(SystemErrorCode.INVALID_PARAM.getHttpCode());
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int size = constraintViolations.size();
        for (ConstraintViolation constraintViolation : constraintViolations) {
            sb.append(constraintViolation.getPropertyPath()).append(": ");
            String messageKey = constraintViolation.getMessageTemplate();
            String message = getLocaleMessage(messageKey, null);
            if (message == null) {
                //默认国际化消息
                message = constraintViolation.getMessage();
            }
            sb.append(message);
            if (size > 1 && i < size - 1) {
                sb.append(", ");
            }
            i++;
        }
        ResponseMessage responseMessage = ResponseMessage.error(SystemErrorCode.INVALID_PARAM, sb.toString());
        responseMessage.setErrDetail(e.getMessage());
        return responseMessage;
    }

    private ResponseMessage handleSystemErrorCode(SystemErrorCode systemErrorCode, String errDetail) {
        response.setStatus(systemErrorCode.getHttpCode());
        String messageKey = "default_" + systemErrorCode.name();
        String message = getLocaleMessage(messageKey, null);
        if(message == null) {
            message = systemErrorCode.getMsg();
        }
        ResponseMessage responseMessage = ResponseMessage.error(systemErrorCode, message);
        responseMessage.setErrDetail(errDetail);
        return responseMessage;
    }

    private ResponseMessage handleGeneralException(GeneralException e) {
        response.setStatus(e.getGeneralErrorCode().getHttpCode());
        String errMessage = getLocaleMessage(e);
        ResponseMessage responseMessage = ResponseMessage.error(e.getGeneralErrorCode(), errMessage);
        responseMessage.setData(e.getData());
        responseMessage.setErrDetail(e.getMessage());
        return responseMessage;
    }

    private String getLocaleMessage(GeneralException e) {
        String messageKey = e.getGeneralErrorCode().getService().toUpperCase() + "_"
                + e.getGeneralErrorCode().getStrCode().toUpperCase();
        String msg = getLocaleMessage(messageKey, e.getArgs());
        if(msg == null) {
            return e.getMessage();
        }
        return msg;
    }

    /**
     * 获取本地化消息
     *
     * @param messageKey
     * @param args
     * @return
     */
    private String getLocaleMessage(String messageKey, Object[] args) {
        String localMessage = null;
        try {
            logger.info("messageKey: {}", messageKey);
            Locale locale = LocaleContextHolder.getLocale();
            localMessage = messageSource.getMessage(messageKey, args, null, locale);
            logger.info("localMessage: {}", localMessage);
        } catch (Exception e) {
            logger.error("get local message error! {}", e);
        }
        return localMessage;
    }

}
