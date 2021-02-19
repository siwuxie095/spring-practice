package com.siwuxie095.spring.chapter9th.example12th.cfg;

import com.siwuxie095.spring.chapter9th.example12th.web.WebConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author Jiajing Li
 * @date 2021-02-19 08:10:03
 */
@SuppressWarnings("all")
public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
