package com.siwuxie095.spring.chapter7th.example2nd;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author Jiajing Li
 * @date 2021-01-31 20:30:24
 */
@SuppressWarnings("all")
public class MyFilterInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic myFilter =
                servletContext.addFilter("myFilter", MyFilter.class);
        myFilter.addMappingForUrlPatterns(null, false, "/custom/*");
    }

}
