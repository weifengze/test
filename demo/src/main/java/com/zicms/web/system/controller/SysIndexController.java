package com.zicms.web.system.controller;

import com.zicms.commons.base.BaseController;
import com.zicms.framework.util.ShiroUtils;
import com.zicms.web.system.model.SysMenu;
import com.zicms.web.system.model.SysUser;
import com.zicms.web.system.service.ISysConfigService;
import com.zicms.web.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 首页 业务处理
 *
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 系统首页
     *
     * @param mmap ModelMap
     * @return
     */
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);
        mmap.put("menus", menus);
        mmap.put("user", user);
        // 页面主体效果 暂去
        mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
        mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
        return "index";
    }

    /**
     * 切换主题
     *
     * @param mmap ModelMap
     * @return
     */
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap) {
        return "skin";
    }

    /**
     * 系统介绍
     *
     * @param mmap ModelMap
     * @return
     */
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        return "main";
    }
}
