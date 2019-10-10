package cloud4myself.com.common.exception.errorcode;

import java.lang.reflect.Method;

public class ErrorCodeClass {
    private Method httpStatusMethod;
    private Method codeMethod;
    private Method messageMethod;
    private String service;
    private Class<?> clazz;

    public ErrorCodeClass(String service, Method httpStatusMethod, Method codeMethod, Method messageMethod, Class<?> clazz) {
        this.service = service;
        this.httpStatusMethod = httpStatusMethod;
        this.codeMethod = codeMethod;
        this.messageMethod = messageMethod;
        this.clazz = clazz;
    }

    public Method getHttpStatusMethod() {
        return httpStatusMethod;
    }

    public void setHttpStatusMethod(Method httpStatusMethod) {
        this.httpStatusMethod = httpStatusMethod;
    }

    public Method getCodeMethod() {
        return codeMethod;
    }

    public void setCodeMethod(Method codeMethod) {
        this.codeMethod = codeMethod;
    }

    public Method getMessageMethod() {
        return messageMethod;
    }

    public void setMessageMethod(Method messageMethod) {
        this.messageMethod = messageMethod;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
