package com.adopt.web;

import dao.AdoptionDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserCenterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            String avatarUrl = null;
            java.sql.Connection c = util.DBUtil.getConnection();
            try {
                java.sql.PreparedStatement ps = c.prepareStatement("SELECT avatar_url FROM users WHERE id=?");
                ps.setLong(1, userId);
                java.sql.ResultSet rs = ps.executeQuery();
                if (rs.next()) avatarUrl = rs.getString(1);
                rs.close();
                ps.close();
            } finally { c.close(); }
            req.setAttribute("avatarUrl", avatarUrl);
            req.setAttribute("applications", new AdoptionDao().listByUser(userId));
            req.setAttribute("submissions", new dao.AnimalSubmissionDao().listByUser(userId));
            req.getRequestDispatcher("/WEB-INF/views/user/center.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
