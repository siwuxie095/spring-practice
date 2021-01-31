package com.siwuxie095.spring.chapter7th.example2nd;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author Jiajing Li
 * @date 2021-01-31 20:14:55
 */
@SuppressWarnings("all")
public class MyServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic myServlet =
                servletContext.addServlet("myServlet", MyServlet.class);
        myServlet.addMapping("/custom/**");
    }

}
