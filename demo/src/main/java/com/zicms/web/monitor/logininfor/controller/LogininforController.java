package com.zicms.web.monitor.logininfor.controller;

import com.zicms.commons.annotation.Log;
import com.zicms.commons.base.BaseController;
import com.zicms.commons.base.JsonResult;
import com.zicms.commons.enums.BusinessType;
import com.zicms.commons.page.TableDataInfo;
import com.zicms.commons.utils.poi.ExcelUtil;
import com.zicms.framework.shiro.service.SysPasswordService;
import com.zicms.web.monitor.logininfor.model.Logininfor;
import com.zicms.web.monitor.logininfor.service.ILogininforService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/logininfor")
public class LogininforController extends BaseController {
    private String prefix = "monitor/logininfor";

    @Autowired
    private ILogininforService logininforService;

    @Autowired
    private SysPasswordService passwordService;

    @RequiresPermissions("monitor:logininfor:view")
    @GetMapping()
    public String logininfor() {
        return prefix + "/logininfor";
    }

    @RequiresPermissions("monitor:logininfor:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Logininfor logininfor) {
        startPage();
        List<Logininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @Log(title = "登陆日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("monitor:logininfor:export")
    @PostMapping("/export")
    @ResponseBody
    public JsonResult export(Logininfor logininfor) {
        List<Logininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<Logininfor> util = new ExcelUtil<Logininfor>(Logininfor.class);
        return util.exportExcel(list, "登陆日志");
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登陆日志", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public JsonResult remove(String ids) {
        return toAjax(logininforService.deleteLogininforByIds(ids));
    }

    @RequiresPermissions("monitor:logininfor:remove")
    @Log(title = "登陆日志", businessType = BusinessType.CLEAN)
    @PostMapping("/clean")
    @ResponseBody
    public JsonResult clean() {
        logininforService.cleanLogininfor();
        return success();
    }

    @RequiresPermissions("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @PostMapping("/unlock")
    @ResponseBody
    public JsonResult unlock(String loginName) {
        passwordService.unlock(loginName);
        return success();
    }
}
