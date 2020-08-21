package com.zicms.web.monitor;

import com.zicms.commons.base.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: bjjh-demo
 * @Description: DruidController
 * @Author: 魏丰泽
 * @Create: 2020-07-29 14:00
 **/
@Controller
@RequestMapping("/monitor/data")
public class DruidController extends BaseController {

    private static final String PREFIX = "/monitor/druid";

    @RequiresPermissions("monitor:data:view")
    @GetMapping()
    public String index() {
        return redirect(PREFIX + "/index");
    }
}
