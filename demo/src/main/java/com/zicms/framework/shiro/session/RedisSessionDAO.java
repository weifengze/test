package com.zicms.framework.shiro.session;

import com.zicms.commons.constant.ShiroConstants;
import com.zicms.commons.enums.OnlineStatus;
import com.zicms.framework.manager.AsyncManager;
import com.zicms.framework.manager.factory.AsyncFactory;
import com.zicms.web.system.service.impl.SysUserOnlineServiceImpl;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @program: bjjh-demo
 * @Description: RedisSessionDAO
 * @Author: 魏丰泽
 * @Create: 2020-07-29 13:23
 **/
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

    @Value("${shiro.session.dbSyncPeriod}")
    private int dbSyncPeriod;

    private int expireTime;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SysUserOnlineServiceImpl onlineService;

    /**
     * 上次同步数据库的时间戳
     */
    private static final String LAST_SYNC_DB_TIMESTAMP = RedisSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 根据会话ID获取会话 先从缓存中获取session
     *
     * @param sessionId 会话ID
     * @return Session
     */
    @Override
    public Session readSession(Serializable sessionId) {
        String key = ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID + sessionId;
        Object obj = redisTemplate.opsForValue().get(key);
        OnlineSession session = (OnlineSession) obj;
        return session;
    }

    /**
     * 创建会话
     *
     * @param session 会话信息
     * @return Serializable
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        this.assignSessionId(session, sessionId);
        String key = ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID + sessionId;
        redisTemplate.opsForValue().set(key, session);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return sessionId;
    }

    // 更新session
    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要更新
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        super.doUpdate(session);
        if (session != null && session.getId() != null) {
            String key = ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID + session.getId();
            redisTemplate.opsForValue().set(key, session);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 当会话过期/停止（如用户退出时）属性等会调用
     */
    @Override
    protected void doDelete(Session session) {
        OnlineSession onlineSession = (OnlineSession) session;
        if (null == onlineSession) {
            return;
        }
        String key = ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID + session.getId();
        boolean result = redisTemplate.delete(key);
        if (result) {
            onlineSession.setStatus(OnlineStatus.off_line);
            onlineService.deleteOnlineById(String.valueOf(onlineSession.getId()));
        }
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    public void syncToDb(OnlineSession onlineSession) {
        Date lastSyncTimestamp = (Date) onlineSession.getAttribute(LAST_SYNC_DB_TIMESTAMP);
        if (lastSyncTimestamp != null) {
            boolean needSync = true;
            long deltaTime = onlineSession.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();
            if (deltaTime < dbSyncPeriod * 60 * 1000) {
                // 时间差不足 无需同步
                needSync = false;
            }
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;

            // session 数据变更了 同步
            if (!isGuest && onlineSession.isAttributeChanged()) {
                needSync = true;
            }

            if (!needSync) {
                return;
            }
        }
        onlineSession.setAttribute(LAST_SYNC_DB_TIMESTAMP, onlineSession.getLastAccessTime());
        // 更新完后 重置标识
        if (onlineSession.isAttributeChanged()) {
            onlineSession.resetAttributeChanged();
        }
        AsyncManager.me().execute(AsyncFactory.syncSessionToDb(onlineSession));
        String key = ShiroConstants.SYS_REDIS_SHIRO_SESSION_ID + onlineSession.getId();
        redisTemplate.opsForValue().set(key, onlineSession);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

}
