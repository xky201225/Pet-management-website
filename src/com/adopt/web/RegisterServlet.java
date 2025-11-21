package com.adopt.web;

import dao.UserDao;
import util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
        String captcha = req.getParameter("captcha");
        String expect = null; javax.servlet.http.HttpSession _s = req.getSession(false);
        if (_s != null) expect = (String) _s.getAttribute("captchaCode");
        if (expect == null || captcha == null || !expect.equalsIgnoreCase(captcha)) {
            req.setAttribute("error", "验证码错误");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        boolean phoneOk = phone != null && phone.matches("\\d{11}");
        boolean pwdOk = password != null && password.length() >= 6 && password.matches("(?s).*[A-Za-z].*") && password.matches("(?s).*\\d.*");
        if (username == null || username.isEmpty()) {
            req.setAttribute("error", "请填写用户名");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        if (!phoneOk) { req.setAttribute("error", "联系方式必须为11位数字"); req.getRequestDispatcher("/register.jsp").forward(req, resp); return; }
        if (!pwdOk) { req.setAttribute("error", "密码至少6位且需同时包含字母与数字"); req.getRequestDispatcher("/register.jsp").forward(req, resp); return; }
        if (!password.equals(confirm)) { req.setAttribute("error", "两次密码不一致"); req.getRequestDispatcher("/register.jsp").forward(req, resp); return; }
        try {
            UserDao userDao = new UserDao();
            if (userDao.existsByUsername(username)) {
                req.setAttribute("error", "用户名已存在");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }
            String hash = HashUtil.sha256(password);
            int rnd = (int)(Math.random()*6) + 1;
            String avatarUrl = "userPicture/" + rnd + ".jpg";
            userDao.create(username, phone, hash, avatarUrl);
            if (_s != null) _s.removeAttribute("captchaCode");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

