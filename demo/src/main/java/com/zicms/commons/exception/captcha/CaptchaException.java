package com.zicms.commons.exception.captcha;

import com.zicms.commons.exception.user.UserException;

/**
 * @program: bjjh-demo
 * @Description: CaptchaException
 * @Author: 魏丰泽
 * @Create: 2020-07-28 14:55
 **/
public class CaptchaException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }

}
