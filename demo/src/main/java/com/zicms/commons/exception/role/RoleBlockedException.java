package com.zicms.commons.exception.role;

import com.zicms.commons.exception.user.UserException;

/**
 * 角色锁定异常类
 *
 * @author ruoyi
 */
public class RoleBlockedException extends UserException {
    private static final long serialVersionUID = 1L;

    public RoleBlockedException() {
        super("role.blocked", null);
    }
}
