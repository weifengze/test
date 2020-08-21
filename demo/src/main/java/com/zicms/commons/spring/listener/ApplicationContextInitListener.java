package com.zicms.commons.spring.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * @program: bjjh-demo
 * @Description: ApplicationContextInitListener
 * @Author: 魏丰泽
 * @Create: 2020-07-28 17:33
 **/
@Component
public class ApplicationContextInitListener implements ApplicationListener<ContextRefreshedEvent>, ServletContextAware {
    private final static Logger logger = LoggerFactory.getLogger(ApplicationContextInitListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    }

    /**
     * @param servletContext
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
    }
}
