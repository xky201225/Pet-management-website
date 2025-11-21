package com.adopt.web;

import dao.KnowledgeDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KnowledgeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String category = req.getParameter("category");
        String keyword = req.getParameter("keyword");
        String id = req.getParameter("id");
        try {
            KnowledgeDao dao = new KnowledgeDao();
            if (id != null) {
                long articleId = Long.parseLong(id);
                javax.servlet.http.HttpSession s = req.getSession(false);
                Long adminId = s == null ? null : (Long) s.getAttribute("adminId");
                Long userId = s == null ? null : (Long) s.getAttribute("userId");
                java.util.Map<String,Object> art = dao.getById(articleId);
                if (art == null) { resp.sendRedirect(req.getContextPath()+"/knowledge"); return; }
                Object st = art.get("status");
                Object owner = art.get("user_id");
                boolean visible = "visible".equals(String.valueOf(st));
                boolean isOwner = userId != null && owner != null && ((Long)owner).equals(userId);
                if (!visible && adminId == null && !isOwner) { resp.sendRedirect(req.getContextPath()+"/knowledge"); return; }
                req.setAttribute("article", art);
                req.setAttribute("comments", dao.listComments(articleId));
                req.getRequestDispatcher("/WEB-INF/views/knowledge/detail.jsp").forward(req, resp);
            } else {
                req.setAttribute("articles", dao.search(category, keyword));
                req.getRequestDispatcher("/WEB-INF/views/knowledge/list.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        javax.servlet.http.HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        Long adminId = s == null ? null : (Long) s.getAttribute("adminId");
        KnowledgeDao dao = new KnowledgeDao();
        try {
            if ("publish".equals(action)) {
                if (userId == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }
                String title = req.getParameter("title");
                String author = (String) s.getAttribute("username");
                String category = req.getParameter("category");
                String content = req.getParameter("content");
                dao.publish(userId, title, author, category, content);
                resp.sendRedirect(req.getContextPath()+"/knowledge");
            } else if ("like".equals(action)) {
                if (userId == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }
                long articleId = Long.parseLong(req.getParameter("id"));
                dao.like(articleId, userId);
                resp.sendRedirect(req.getContextPath()+"/knowledge?id="+articleId);
            } else if ("comment".equals(action)) {
                if (userId == null) { resp.sendRedirect(req.getContextPath()+"/login"); return; }
                long articleId = Long.parseLong(req.getParameter("id"));
                String content = req.getParameter("content");
                dao.comment(articleId, userId, content);
                resp.sendRedirect(req.getContextPath()+"/knowledge?id="+articleId);
            } else if ("delete".equals(action)) {
                long articleId = Long.parseLong(req.getParameter("id"));
                boolean ok = dao.delete(articleId, userId, adminId);
                resp.sendRedirect(req.getContextPath()+"/knowledge"+(ok?"":"?error=无权限或文章不存在"));
            } else {
                resp.sendRedirect(req.getContextPath()+"/knowledge");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
