package com.siwuxie095.spring.chapter7th.example2nd;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Jiajing Li
 * @date 2021-01-31 20:31:42
 */
@SuppressWarnings("all")
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
