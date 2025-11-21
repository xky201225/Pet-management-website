package com.adopt.web;

import dao.ForumDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ForumServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String category = req.getParameter("category");
        String keyword = req.getParameter("keyword");
        try {
            ForumDao dao = new ForumDao();
            if (idParam != null && !idParam.isEmpty()) {
                long id = Long.parseLong(idParam);
                java.util.List<java.util.Map<String,Object>> one = new java.util.ArrayList<>();
                for (java.util.Map<String,Object> t : dao.listTopics(null, null)) { if (((Long)t.get("id")).longValue()==id) { one.add(t); break; } }
                java.util.List<java.util.Map<String,Object>> comments = dao.listComments(id);
                if (!one.isEmpty()) req.setAttribute("topic", one.get(0));
                req.setAttribute("comments", comments);
                req.getRequestDispatcher("/WEB-INF/views/forum/detail.jsp").forward(req, resp);
            } else {
                java.util.List<java.util.Map<String,Object>> topics = dao.listTopics(category, keyword);
                req.setAttribute("topics", topics);
                req.getRequestDispatcher("/WEB-INF/views/forum/list.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
            try {
                ForumDao dao = new ForumDao();
                if ("create".equals(action)) {
                String title = req.getParameter("title");
                String category = req.getParameter("category");
                String content = req.getParameter("content");
                if (title == null || title.trim().isEmpty() || category == null || category.trim().isEmpty() || content == null || content.trim().isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/forum?error=请输入完整的标题/分类/内容");
                    return;
                }
                dao.createTopic(userId, title.trim(), category.trim(), content.trim(), null);
                } else if ("like".equals(action)) {
                    long topicId = Long.parseLong(req.getParameter("topicId"));
                    dao.like(topicId, userId);
                    String ret = req.getParameter("returnDetail");
                    if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath() + "/forum?id=" + topicId); return; }
                } else if ("comment".equals(action)) {
                    long topicId = Long.parseLong(req.getParameter("topicId"));
                    String content = req.getParameter("content");
                    if (content == null || content.trim().isEmpty()) {
                        String ret = req.getParameter("returnDetail");
                        if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath() + "/forum?id=" + topicId + "&error=评论不能为空"); return; }
                        resp.sendRedirect(req.getContextPath() + "/forum?error=评论不能为空");
                        return;
                    }
                    dao.comment(topicId, userId, content.trim());
                    String ret = req.getParameter("returnDetail");
                    if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath() + "/forum?id=" + topicId); return; }
                }
                resp.sendRedirect(req.getContextPath() + "/forum");
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }
