package com.adopt.web;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class UserProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        try (Connection conn = DBUtil.getConnection()) {
            long uid = Long.parseLong(idParam);
            Map<String,Object> user = new HashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id,username,avatar_url,phone,created_at FROM users WHERE id=?")) {
                ps.setLong(1, uid);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) {
                    user.put("id", rs.getLong("id"));
                    user.put("username", rs.getString("username"));
                    user.put("avatar_url", rs.getString("avatar_url"));
                    user.put("phone", rs.getString("phone"));
                    user.put("created_at", rs.getTimestamp("created_at"));
                }}
            }
            long addCount = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM animal_submissions WHERE user_id=?")) {
                ps.setLong(1, uid); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) addCount = rs.getLong(1); }
            }
            long adoptOk = 0;
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM adoption_applications WHERE user_id=? AND status='批准'")) {
                ps.setLong(1, uid); try (ResultSet rs = ps.executeQuery()) { if (rs.next()) adoptOk = rs.getLong(1); }
            }
            List<Map<String,Object>> topics = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id,title,created_at FROM topics WHERE user_id=? ORDER BY created_at DESC")) {
                ps.setLong(1, uid); try (ResultSet rs = ps.executeQuery()) { while (rs.next()) {
                    Map<String,Object> m = new HashMap<>(); m.put("id", rs.getLong(1)); m.put("title", rs.getString(2)); m.put("created_at", rs.getTimestamp(3)); topics.add(m);
                }}
            }
            List<Map<String,Object>> articles = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT id,title,published_at FROM knowledge_articles WHERE user_id=? ORDER BY published_at DESC")) {
                ps.setLong(1, uid); try (ResultSet rs = ps.executeQuery()) { while (rs.next()) {
                    Map<String,Object> m = new HashMap<>(); m.put("id", rs.getLong(1)); m.put("title", rs.getString(2)); m.put("published_at", rs.getTimestamp(3)); articles.add(m);
                }}
            }
            req.setAttribute("user", user);
            req.setAttribute("addCount", addCount);
            req.setAttribute("adoptOk", adoptOk);
            req.setAttribute("topics", topics);
            req.setAttribute("articles", articles);
            req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}