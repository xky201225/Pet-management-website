package com.adopt.web;

import dao.AdminDao;
import dao.UserDao;
import util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String captcha = req.getParameter("captcha");
        String expect = null;
        javax.servlet.http.HttpSession _s = req.getSession(false);
        if (_s != null) expect = (String) _s.getAttribute("captchaCode");
        if (expect == null || captcha == null || !expect.equalsIgnoreCase(captcha)) {
            req.setAttribute("error", "验证码错误");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }
        String hash = HashUtil.sha256(password);
        try {
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                req.setAttribute("error", "请填写用户名和密码");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
            if ("admin".equals(role)) {
                Long id = new AdminDao().authenticate(username, hash);
                if (id != null) {
                    HttpSession s = req.getSession(true);
                    s.setAttribute("adminId", id);
                    s.setAttribute("adminUsername", username);
                    s.removeAttribute("captchaCode");
                    resp.sendRedirect(req.getContextPath() + "/admin");
                    return;
                }
                req.setAttribute("error", "管理员用户名或密码错误");
            } else {
                Long id = new UserDao().authenticate(username, hash);
                if (id != null) {
                    HttpSession s = req.getSession(true);
                    s.setAttribute("userId", id);
                    s.setAttribute("username", username);
                    try { String avu = new UserDao().getAvatarUrl(id); if (avu != null) s.setAttribute("avatarUrl", avu); } catch (Exception ignore) {}
                    s.removeAttribute("captchaCode");
                    resp.sendRedirect(req.getContextPath() + "/");
                    return;
                }
                req.setAttribute("error", "用户用户名或密码错误");
            }
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

