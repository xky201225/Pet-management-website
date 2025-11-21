package com.adopt.web;

import dao.UserDao;
import util.HashUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

@MultipartConfig
public class UserUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        if (userId == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }
        String username = req.getParameter("username");
        String phone = req.getParameter("phone");
        String oldPwd = req.getParameter("old_password");
        String newPwd = req.getParameter("new_password");
        String confirmPwd = req.getParameter("confirm_password");
        String avatarUrl = null;
        try {
            // handle avatar upload
            Part avatar = null;
            try { avatar = req.getPart("avatar"); } catch (Exception ignore) {}
            if (avatar != null && avatar.getSize() > 0) {
                String original = avatar.getSubmittedFileName();
                String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.')) : ".png";
                String fn = "avatar_user_"+userId+ext;

                java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
                try (java.io.InputStream in = avatar.getInputStream()) {
                    byte[] tmp = new byte[8192]; int n; while ((n = in.read(tmp)) > 0) buf.write(tmp, 0, n);
                }
                byte[] data = buf.toByteArray();

                String real = req.getServletContext().getRealPath("/userPicture");
                File rd = new File(real);
                if (!rd.exists()) rd.mkdirs();
                java.nio.file.Files.write(new File(rd, fn).toPath(), data);

                File base = new File(req.getServletContext().getRealPath("/"));
                File cur = base;
                File project = null;
                for (int i = 0; i < 10 && cur != null; i++) {
                    File w = new File(cur, "web");
                    if (w.exists() && w.isDirectory()) { project = cur; break; }
                    cur = cur.getParentFile();
                }
                if (project == null) {
                    File u = new File(System.getProperty("user.dir"));
                    if (new File(u, "web").exists()) project = u;
                }
                if (project != null) {
                    File pd = new File(project, "web/userPicture");
                    if (!pd.exists()) pd.mkdirs();
                    java.nio.file.Files.write(new File(pd, fn).toPath(), data);
                }

                avatarUrl = "userPicture/"+fn;
            }
            String usernameVal = (username == null || username.trim().isEmpty()) ? null : username.trim();
            String phoneVal = (phone == null || phone.trim().isEmpty()) ? null : phone.trim();
            UserDao dao = new UserDao();
            if (usernameVal != null) {
                String current = (String) s.getAttribute("username");
                if (!usernameVal.equals(current) && dao.existsByUsername(usernameVal)) {
                    req.setAttribute("error", "用户名已存在");
                    req.getRequestDispatcher("/WEB-INF/views/user/center.jsp").forward(req, resp);
                    return;
                }
            }
            // update profile
            java.sql.Connection conn = util.DBUtil.getConnection();
            try {
                java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE users SET username=COALESCE(?,username), phone=COALESCE(?,phone), avatar_url=COALESCE(?,avatar_url) WHERE id=?");
                ps.setString(1, usernameVal);
                ps.setString(2, phoneVal);
                ps.setString(3, avatarUrl);
                ps.setLong(4, userId);
                ps.executeUpdate();
                ps.close();
            } finally { conn.close(); }
            if (usernameVal != null) s.setAttribute("username", usernameVal);
            // update password if provided
            if (oldPwd != null && !oldPwd.isEmpty() && newPwd != null && !newPwd.isEmpty() && newPwd.equals(confirmPwd)) {
                String oldHash = HashUtil.sha256(oldPwd);
                String newHash = HashUtil.sha256(newPwd);
                java.sql.Connection c2 = util.DBUtil.getConnection();
                try {
                    java.sql.PreparedStatement check = c2.prepareStatement("SELECT 1 FROM users WHERE id=? AND password_hash=?");
                    check.setLong(1, userId);
                    check.setString(2, oldHash);
                    java.sql.ResultSet rs = check.executeQuery();
                    boolean ok = rs.next();
                    rs.close();
                    check.close();
                    if (!ok) {
                        req.setAttribute("error", "原密码错误");
                        req.getRequestDispatcher("/WEB-INF/views/user/center.jsp").forward(req, resp);
                        return;
                    }
                    java.sql.PreparedStatement upd = c2.prepareStatement("UPDATE users SET password_hash=? WHERE id=?");
                    upd.setString(1, newHash);
                    upd.setLong(2, userId);
                    upd.executeUpdate();
                    upd.close();
                } finally { c2.close(); }
            }
            resp.sendRedirect(req.getContextPath()+"/user");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
