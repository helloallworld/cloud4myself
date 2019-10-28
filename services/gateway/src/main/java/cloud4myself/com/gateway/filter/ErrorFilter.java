package cloud4myself.com.gateway.filter;

import cloud4myself.com.common.exception.GeneralException;
import cloud4myself.com.common.exception.SystemErrorCode;
import cloud4myself.com.common.web.ResponseMessage;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 网关服务异常拦截
 */
@Component
public class ErrorFilter extends ZuulFilter {
    private final static Logger logger= LoggerFactory.getLogger(ErrorFilter.class);
    @Autowired
    private MessageSource messageSource;

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return -9999;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ctx.set("error.exception", throwable.getCause());
        ctx.remove("throwable");
        logger.error("Gateway filter error, method: " + ctx.getRequest().getMethod()
                + ", url: " + ctx.getRequest().getRequestURL(), throwable);

        int httpStatus = 500;
        String service = "default";
        Object data = null;
        int errCode = SystemErrorCode.SYSTEM_ERROR.getCode();
        String errMsg = null;
        String errDetail = null;

        while(throwable != null) {
            if(throwable instanceof GeneralException) {
                GeneralException e = (GeneralException)throwable;
                service = e.getGeneralErrorCode().getService();
                errDetail = e.getGeneralErrorCode().getMessage();
                errMsg = getLocaleMessage(e);
                errCode = e.getGeneralErrorCode().getCode();
                data = e.getData();
                httpStatus = e.getGeneralErrorCode().getHttpCode();
                break;
            } else {
                if(errDetail == null) {
                    errDetail = throwable.getMessage();
                } else {
                    errDetail = errDetail + "; " + throwable.getMessage();
                }

                if(throwable.getMessage().toLowerCase().contains("forwarding error")) {
                    errMsg = messageSource.getMessage("default_" + SystemErrorCode.FORWARDING_ERROR, null,
                            null, LocaleContextHolder.getLocale());
                    httpStatus = SystemErrorCode.FORWARDING_ERROR.getHttpCode();
                }

                if(throwable.getMessage().toLowerCase().contains("timeout")) {
                    errMsg = messageSource.getMessage("default_" + SystemErrorCode.GATEWAY_TIMEOUT, null,
                            null, LocaleContextHolder.getLocale());
                    httpStatus = SystemErrorCode.GATEWAY_TIMEOUT.getHttpCode();
                }

                if(throwable.getMessage().toLowerCase().contains("connection refused")) {
                    errMsg = messageSource.getMessage("default_" + SystemErrorCode.FORWARDING_ERROR, null,
                            null, LocaleContextHolder.getLocale());
                    httpStatus = SystemErrorCode.FORWARDING_ERROR.getHttpCode();
                }
            }
            throwable = throwable.getCause();
        }

        if(errMsg == null) {
            errMsg = messageSource.getMessage("default_" + SystemErrorCode.SYSTEM_ERROR.name(), null,
                    null, LocaleContextHolder.getLocale());
            httpStatus = 500;
        }

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setService(service);
        responseMessage.setErrCode(errCode);
        responseMessage.setErrMsg(errMsg);

        // 忽略ErrDetail，否则会暴露系统内部数据
        logger.error("ErrorDetail: " + errDetail);
        responseMessage.setErrDetail(null);

        responseMessage.setData(data);
        ctx.setResponseStatusCode(httpStatus);
//        ctx.setResponseBody(FastJsonUtil.toJSON(responseMessage));
        Gson gson=new Gson();
        ctx.setResponseBody(gson.toJson(responseMessage));
        ctx.addZuulResponseHeader("Content-Type", "application/json;charset=utf-8");

        return null;
    }

    private String getLocaleMessage(GeneralException e) {
        String messageKey = null;
        if(!e.getGeneralErrorCode().getService().toLowerCase().equalsIgnoreCase("default")) {
            messageKey = e.getGeneralErrorCode().getService().toUpperCase() + "_"
                    + e.getGeneralErrorCode().getStrCode().toUpperCase();
        } else {
            messageKey = "default_" + e.getGeneralErrorCode().getStrCode().toUpperCase();
        }
        String msg = null;
        try {
            Locale locale = LocaleContextHolder.getLocale();
            msg = messageSource.getMessage(messageKey, e.getArgs(), null, locale);
        } catch (Exception e1) {
            logger.error("get local message error! {}", e1);
        }
        if(msg == null) {
            return e.getMessage();
        }
        return msg;
    }
}
