package com.siwuxie095.spring.chapter7th.example2nd;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Jiajing Li
 * @date 2021-01-31 20:17:02
 */
@SuppressWarnings("all")
public class MyServlet implements Servlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
