package com.zicms.commons.exception;

/**
 * @program: bjjh-bl-plt
 * @Description: BusinessException
 * @Author: 魏丰泽
 * @Create: 2020-07-27 12:52
 **/
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5103108281149105211L;

    protected final String message;

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
