package com.adopt.web;

import dao.AdoptionDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdoptionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            AdoptionDao dao = new AdoptionDao();
            req.setAttribute("applications", dao.listByUser(userId));
            req.getRequestDispatcher("/WEB-INF/views/adoption/status.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String animalIdStr = req.getParameter("animalId");
        String residence = req.getParameter("residence");
        String experience = req.getParameter("experience");
        String message = req.getParameter("message");
        try {
            long animalId = Long.parseLong(animalIdStr);
            // 校验该动物是否已被领养
            java.util.Map<String,Object> a = new dao.AnimalDao().getById(animalId);
            if (a != null) {
                Object ad = a.get("adopted");
                boolean adopted = ad instanceof Boolean ? (Boolean) ad : ("1".equals(String.valueOf(ad)));
                if (adopted) {
                    req.setAttribute("error", "该动物已被领养，无法提交申请");
                    req.setAttribute("animal", a);
                    req.getRequestDispatcher("/WEB-INF/views/animals/detail.jsp").forward(req, resp);
                    return;
                }
            }
            new AdoptionDao().create(userId, animalId, residence, experience, message);
            resp.sendRedirect(req.getContextPath() + "/adoption");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
