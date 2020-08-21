package com.zicms;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @program: bjjh-bl-plt
 * @Description: SpringDemoServletInitializer web容器中进行部署
 * @Author: 魏丰泽
 * @Create: 2020-07-24 17:18
 **/
public class SpringDemoServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }
}
