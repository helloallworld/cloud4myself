package cloud4myself.com.common.exception.errorcode;

import cloud4myself.com.common.exception.SystemErrorCode;
import cloud4myself.com.common.exception.errorcode.annotation.ECGetCode;
import cloud4myself.com.common.exception.errorcode.annotation.ECGetHTTPStatus;
import cloud4myself.com.common.exception.errorcode.annotation.ECGetMessage;
import cloud4myself.com.common.exception.errorcode.annotation.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ErrorCodeFactory {
    private static Map<Class<?>, ErrorCodeClass> errorCodeClassMap = null;
    private static final Logger logger = LoggerFactory.getLogger(ErrorCodeFactory.class);

    public static GeneralErrorCode createErrorCode(Object errorCode) {
        return ErrorCodeFactory.createErrorCode(errorCode, null);
    }

    public static GeneralErrorCode createErrorCode(Object errorCode, String message) {
        if(ErrorCodeFactory.errorCodeClassMap == null) {
            ErrorCodeFactory.loadErrorCodes();
        }

        ErrorCodeClass errorCodeClass = ErrorCodeFactory.errorCodeClassMap.get(errorCode.getClass());
        if(errorCodeClass == null) {
            errorCodeClass = createErrorCodeClass(errorCode.getClass());
            if(errorCodeClass == null) {
                throw new RuntimeException("class " + errorCode.getClass() + " is not an @ErrorCode class");
            }
            synchronized (ErrorCodeFactory.class) {
                ErrorCodeFactory.errorCodeClassMap.put(errorCode.getClass(), errorCodeClass);
            }
        }

        GeneralErrorCode generalErrorCode = new GeneralErrorCode();
        try {
            generalErrorCode.setCode((Integer)errorCodeClass.getCodeMethod().invoke(errorCode));
            generalErrorCode.setHttpCode((Integer)errorCodeClass.getHttpStatusMethod().invoke(errorCode));
            if(message == null) {
                generalErrorCode.setMessage((String) errorCodeClass.getMessageMethod().invoke(errorCode));
            } else {
                generalErrorCode.setMessage(message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        generalErrorCode.setService(errorCodeClass.getService());
        generalErrorCode.setStrCode(errorCode.toString());

        return generalErrorCode;
    }

    private static Method getMethod(Method method, Class<?> clazz, Class<?> returnType) throws Exception {
        if(method.getReturnType() != returnType) {
            throw new Exception("Method " + method.getName() + " in class " + clazz + " should return type " + returnType);
        }
        if(method.getParameterCount() != 0) {
            throw new Exception("Method " + method.getName() + " in class " + clazz + " shouldn't take any parameter");
        }
        method.setAccessible(true);
        return method;
    }

    private static synchronized void loadErrorCodes() {
        if(ErrorCodeFactory.errorCodeClassMap != null) {
            return;
        }

        Map<Class<?>, ErrorCodeClass> map = new HashMap<>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ErrorCode.class));
        for(BeanDefinition bd : scanner.findCandidateComponents("com.ink.snow.*")) {
            try {
                logger.info("Processing @ErrorCode class " + bd.getBeanClassName());
                Class<?> clazz = ErrorCode.class.getClassLoader().loadClass(bd.getBeanClassName());
                ErrorCodeClass errorCodeClass = createErrorCodeClass(clazz);
                if(errorCodeClass == null) {
                    continue;
                }
                map.put(clazz, errorCodeClass);
                logger.info("Registered @ErrorCode class " + clazz);
            } catch (Exception e) {
                logger.error("{}", e);
            }
        }

        ErrorCodeFactory.errorCodeClassMap = map;
    }

    private static ErrorCodeClass createErrorCodeClass(Class<?> clazz) {
        try {
            String service = clazz.getAnnotation(ErrorCode.class).value();
            if (service.equals("SYSTEM")) {
                if (!clazz.equals(SystemErrorCode.class)) {
                    logger.error("Invalid @ErrorCode class named 'SYSTEM', " + clazz);
                    return null;
                }
            }
            Method httpStatusMethod = null;
            Method codeMethod = null;
            Method messageMethod = null;
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(ECGetHTTPStatus.class)) {
                    httpStatusMethod = getMethod(method, clazz, Integer.class);
                } else if (method.isAnnotationPresent(ECGetCode.class)) {
                    codeMethod = getMethod(method, clazz, Integer.class);
                } else if (method.isAnnotationPresent(ECGetMessage.class)) {
                    messageMethod = getMethod(method, clazz, String.class);
                }
            }
            if (httpStatusMethod == null) {
                logger.error("Missing @ECGetHTTPStatus annotation in class " + clazz.toString());
                return null;
            }
            if (codeMethod == null) {
                logger.error("Missing @ECGetCode annotation in class " + clazz.toString());
                return null;
            }
            if (messageMethod == null) {
                logger.error("Missing @ECGetMessage annotation in class " + clazz.toString());
                return null;
            }

            return new ErrorCodeClass(service, httpStatusMethod, codeMethod, messageMethod, clazz);
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }
}
