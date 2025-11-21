package com.adopt.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import dao.AnimalDao;
import dao.ForumDao;
import dao.KnowledgeDao;

public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Map<String, Object>> animals = new ArrayList<>();
            for (Map<String, Object> a : new AnimalDao().listAll()) {
                Object ad = a.get("adopted");
                boolean adopted = ad instanceof Boolean ? (Boolean) ad : ("1".equals(String.valueOf(ad)));
                if (!adopted) animals.add(a);
            }
            req.setAttribute("animals", animals);
            List<Map<String,Object>> topics = new ForumDao().listTopics(null, null);
            req.setAttribute("topics", topics);
            List<Map<String,Object>> articles = new KnowledgeDao().search(null, null);
            req.setAttribute("articles", articles);
        } catch (Exception ignored) {}
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
