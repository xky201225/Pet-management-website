package com.adopt.web;

import dao.AnimalDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AnimalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String source = req.getParameter("source");
        try {
            AnimalDao dao = new AnimalDao();
            if (idParam == null || idParam.isEmpty() || "null".equalsIgnoreCase(idParam)) {
                req.setAttribute("animals", dao.listAll());
                req.getRequestDispatcher("/WEB-INF/views/animals/list.jsp").forward(req, resp);
            } else {
                long id;
                try { id = Long.parseLong(idParam); } catch (NumberFormatException nfe) {
                    resp.sendRedirect(req.getContextPath() + "/animals");
                    return;
                }
                if ("submission" != null && "submission".equals(source)) {
                    dao.AnimalSubmissionDao sdao = new dao.AnimalSubmissionDao();
                    java.util.Map<String,Object> sub = sdao.getById(id);
                    req.setAttribute("animal", sub);
                } else {
                    req.setAttribute("animal", dao.getById(id));
                }
                req.getRequestDispatcher("/WEB-INF/views/animals/detail.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
