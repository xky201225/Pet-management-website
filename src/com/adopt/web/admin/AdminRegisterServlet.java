package com.adopt.web.admin;

import dao.AdminDao;
import util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminRegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/admin_register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String username = req.getParameter("username");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
        try {
            AdminDao dao = new AdminDao();
            boolean firstAdmin = dao.isEmpty();
            boolean ok = false;
            if (firstAdmin) {
                ok = "123456".equals(code);
            } else {
                ok = code != null && dao.inviteExists(code);
            }
            if (!ok) {
                req.setAttribute("error", "邀请码错误");
                req.getRequestDispatcher("/admin_register.jsp").forward(req, resp);
                return;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        boolean phoneOk = phone != null && phone.matches("\\d{11}");
        boolean pwdOk = password != null && password.length() >= 6 && password.matches("(?s).*[A-Za-z].*") && password.matches("(?s).*\\d.*");
        if (username == null || username.isEmpty()) { req.setAttribute("error", "请填写用户名"); req.getRequestDispatcher("/admin_register.jsp").forward(req, resp); return; }
        if (!phoneOk) { req.setAttribute("error", "联系方式必须为11位数字"); req.getRequestDispatcher("/admin_register.jsp").forward(req, resp); return; }
        if (!pwdOk) { req.setAttribute("error", "密码至少6位且需同时包含字母与数字"); req.getRequestDispatcher("/admin_register.jsp").forward(req, resp); return; }
        if (!password.equals(confirm)) { req.setAttribute("error", "两次密码不一致"); req.getRequestDispatcher("/admin_register.jsp").forward(req, resp); return; }
        try {
            AdminDao dao = new AdminDao();
            if (dao.existsByUsername(username)) {
                req.setAttribute("error", "用户名已存在");
                req.getRequestDispatcher("/admin_register.jsp").forward(req, resp);
                return;
            }
            int rnd = (int)(Math.random()*6) + 1;
            String avatarUrl = "userPicture/" + rnd + ".jpg";
            dao.create(username, phone, HashUtil.sha256(password), avatarUrl);
            resp.sendRedirect(req.getContextPath() + "/login?role=admin");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
