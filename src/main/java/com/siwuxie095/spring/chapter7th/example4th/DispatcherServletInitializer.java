package com.siwuxie095.spring.chapter7th.example4th;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * @author Jiajing Li
 * @date 2021-02-01 22:08:56
 */
@SuppressWarnings("all")
public class DispatcherServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        DispatcherServlet ds = new DispatcherServlet();
        ServletRegistration.Dynamic registration =
                servletContext.addServlet("appServlet", ds);
        registration.addMapping("/");
        registration.setMultipartConfig(
                new MultipartConfigElement("/tmp/spittr/uploads"));
    }

}
