package cloud4myself.com.common.util;

import cloud4myself.com.common.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * 提供读取当前用户信息的快捷接口，依赖网关将用户信息插入HTTP Headers中实现
 */
public class HttpHelper {
    public static String getCurrentURI() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRequestURI();
    }

    public static String getToken() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getParameter("token");
    }

    public static String getUserAgent() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_USER_AGENT);
    }

    public static String getClientIP() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        if(request.getHeader(Constants.HTTP_HEADER_X_FORWARD_FOR) != null) {
            return request.getHeader(Constants.HTTP_HEADER_X_FORWARD_FOR).split(",")[0];
        }
        if(request.getHeader(Constants.HTTP_HEADER_X_REAL_IP) != null) {
            return request.getHeader(Constants.HTTP_HEADER_X_REAL_IP);
        }
        return request.getRemoteAddr();
    }

    public static String getToken(HttpServletRequest request) {
        return request.getParameter("token");
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(Constants.HTTP_HEADER_USER_AGENT);
    }

    public static String getClientIP(HttpServletRequest request) {
        if(request.getHeader(Constants.HTTP_HEADER_X_FORWARD_FOR) != null) {
            return request.getHeader(Constants.HTTP_HEADER_X_FORWARD_FOR).split(",")[0];
        }
        if(request.getHeader(Constants.HTTP_HEADER_X_REAL_IP) != null) {
            return request.getHeader(Constants.HTTP_HEADER_X_REAL_IP);
        }
        return request.getRemoteAddr();
    }

    /**
     * 获取当前登录用户的用户ID，需要依赖网关
     *
     * @return 用户ID
     */
    public static Long getCurrentAccountId() {
        String tmp = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_USER_ACCOUNT_ID);
        try {
            return Long.parseLong(tmp);
        } catch (Exception e) {
            return -1L;
        }
    }

    /**
     * 获取当前登录用户的账号名，需要依赖网关
     *
     * @return 账号名
     */
    public static String getCurrentUsername() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_USER_USERNAME);
    }

    /**
     * 获取当前登录用户的用户姓名，需要依赖网关
     *
     * @return 用户姓名
     */
    public static String getCurrentNickname() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_USER_NICKNAME);
    }

    /**
     * 获取当前登录用户的所属部门，需要依赖网关
     *
     * @return 所属部门
     */
    public static String getCurrentAccountDepartment() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_USER_DEPARTMENT);
    }

    /**
     * 获取当前登录用户的邮箱，需要依赖网关
     *
     * @return 邮箱
     */
    public static String getCurrentAccountEmail() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_USER_EMAIL);
    }

    /**
     * 获取当前登录用户的电话，需要依赖网关
     *
     * @return 电话号码
     */
    public static String getCurrentAccountTelephone() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_USER_TELEPHONE);
    }

    /**
     * 获取当前登录用户所属的角色ID，需要依赖网关
     *
     * @return 角色ID
     */
    public static Long getCurrentRoleId() {
        String tmp = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_ROLE_ID);
        try {
            return Long.parseLong(tmp);
        } catch (Exception e) {
            return -1L;
        }
    }

    /**
     * 获取当前登录用户所属的角色名，需要依赖网关
     *
     * @return 角色名
     */
    public static String getCurrentRoleName() {
        return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_ROLE_NAME);
    }

    public static Long getCurrentFunctionId() {
        String tmp = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_FUNCTION_ID);
        try {
            return Long.parseLong(tmp);
        } catch (Exception e) {
            return -1L;
        }
    }

    public static String getCurrentFunctionName() {
        String tmp = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                .getHeader(Constants.HTTP_HEADER_FUNCTION_NAME);
        try {
            return URLDecoder.decode(tmp, "utf-8");
        } catch (Exception e) {
            return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getHeader(Constants.HTTP_HEADER_FUNCTION_NAME_EN);
        }
    }
}