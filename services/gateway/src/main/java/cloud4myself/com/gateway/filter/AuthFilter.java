package cloud4myself.com.gateway.filter;

import cloud4myself.com.common.exception.GeneralException;
import cloud4myself.com.common.exception.SystemErrorCode;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("begin myFilter");
        //RequestContext中的数据可用于各个模块间调用
        RequestContext context=RequestContext.getCurrentContext();
        HttpServletRequest request=context.getRequest();
        String method=request.getMethod();
        String uri=request.getRequestURI();
//        if(uri.contains("sayhello")) {
//            throw new GeneralException(SystemErrorCode.MEDIA_NOT_SUPPORTED);
//        }
//        if(method.equals("GET")){
//           context.setSendZuulResponse(true);
//            return null;
//        }
//        context.setSendZuulResponse(false);

        System.out.println("end myFilter");
        return null;
    }
}
