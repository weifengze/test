package com.zicms.web.monitor.online.controller;

import com.zicms.commons.annotation.Log;
import com.zicms.commons.base.BaseController;
import com.zicms.commons.base.JsonResult;
import com.zicms.commons.enums.BusinessType;
import com.zicms.commons.enums.OnlineStatus;
import com.zicms.commons.page.TableDataInfo;
import com.zicms.commons.utils.text.Convert;
import com.zicms.framework.shiro.session.OnlineSessionDAO;
import com.zicms.framework.util.ShiroUtils;
import com.zicms.web.monitor.online.model.OnlineSession;
import com.zicms.web.monitor.online.model.UserOnline;
import com.zicms.web.monitor.online.service.IUserOnlineService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 在线用户监控
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/online")
public class UserOnlineController extends BaseController {
    private String prefix = "monitor/online";

    @Autowired
    private IUserOnlineService userOnlineService;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    @RequiresPermissions("monitor:online:view")
    @GetMapping()
    public String online() {
        return prefix + "/online";
    }

    @RequiresPermissions("monitor:online:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserOnline userOnline) {
        startPage();
        List<UserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        return getDataTable(list);
    }

    @RequiresPermissions(value = {"monitor:online:batchForceLogout", "monitor:online:forceLogout"}, logical = Logical.OR)
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @PostMapping("/batchForceLogout")
    @ResponseBody
    public JsonResult batchForceLogout(String ids) {
        for (String sessionId : Convert.toStrArray(ids)) {
            UserOnline online = userOnlineService.selectOnlineById(sessionId);
            if (online == null) {
                return error("用户已下线");
            }
            OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
            if (onlineSession == null) {
                return error("用户已下线");
            }
            if (sessionId.equals(ShiroUtils.getSessionId())) {
                return error("当前登陆用户无法强退");
            }
            onlineSession.setStatus(OnlineSession.OnlineStatus.off_line);
            onlineSessionDAO.update(onlineSession);
            online.setStatus(OnlineStatus.off_line);
            userOnlineService.saveOnline(online);
        }
        return success();
    }
}
