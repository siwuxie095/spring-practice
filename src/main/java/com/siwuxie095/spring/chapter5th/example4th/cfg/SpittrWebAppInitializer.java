package com.siwuxie095.spring.chapter5th.example4th.cfg;

import com.siwuxie095.spring.chapter5th.example4th.web.WebConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 配置 DispatcherServlet
 *
 * @author Jiajing Li
 * @date 2021-01-20 21:51:04
 */
@SuppressWarnings("all")
public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { RootConfig.class };
    }

    // 指定配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    // 将 DispatcherServlet 映射到 "/"
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
