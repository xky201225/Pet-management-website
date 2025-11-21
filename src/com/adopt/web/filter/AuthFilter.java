package com.adopt.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String ctx = req.getContextPath();

        boolean allow = false;
        if (uri.startsWith(ctx + "/static") || uri.startsWith(ctx + "/picture") || uri.startsWith(ctx + "/animalsPicture") || uri.startsWith(ctx + "/userPicture") || uri.startsWith(ctx + "/captcha") || uri.startsWith(ctx + "/animals") || uri.startsWith(ctx + "/user/profile") || uri.equals(ctx + "/") || uri.equals(ctx + "/home") || uri.equals(ctx + "/login") || uri.equals(ctx + "/register") || uri.equals(ctx + "/admin/register")) {
            allow = true;
        }

        HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        Long adminId = s == null ? null : (Long) s.getAttribute("adminId");

        if (!allow) {
            if (uri.startsWith(ctx + "/admin")) {
                if (adminId == null) {
                    resp.sendRedirect(ctx + "/login?role=admin");
                    return;
                }
            } else {
                if (userId == null) {
                    resp.sendRedirect(ctx + "/login");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
