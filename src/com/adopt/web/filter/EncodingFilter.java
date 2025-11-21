package com.adopt.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}

