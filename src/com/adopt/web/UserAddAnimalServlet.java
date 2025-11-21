package com.adopt.web;

import dao.AnimalSubmissionDao;

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
public class UserAddAnimalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/user/add_animal.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Long userId = s == null ? null : (Long) s.getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        try {
            String name = req.getParameter("name");
            String species = req.getParameter("species");
            String ageStr = req.getParameter("age");
            if (name == null || name.isEmpty() || species == null || species.isEmpty() || ageStr == null || ageStr.isEmpty()) {
                req.setAttribute("msg", "请完整填写必填项");
                req.getRequestDispatcher("/WEB-INF/views/user/add_animal.jsp").forward(req, resp);
                return;
            }
            int age;
            try { age = Integer.parseInt(ageStr); } catch (NumberFormatException e) {
                req.setAttribute("msg", "年龄必须是数字");
                req.getRequestDispatcher("/WEB-INF/views/user/add_animal.jsp").forward(req, resp);
                return;
            }
            String dir = getServletContext().getRealPath("/animalsPicture");
            File d = new File(dir); if (!d.exists()) d.mkdirs();
            Part photo = null; try { photo = req.getPart("photo_file"); } catch (Exception ignore) {}
            String photoPath = null;
            if (photo != null && photo.getSize() > 0) {
                String original = photo.getSubmittedFileName();
                String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.')) : ".png";
                String fn = "animal_sub_"+System.currentTimeMillis()+ext;
                java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
                try (java.io.InputStream in = photo.getInputStream()) { byte[] tmp = new byte[8192]; int n; while((n=in.read(tmp))>0) buf.write(tmp,0,n);} byte[] data = buf.toByteArray();
                java.nio.file.Files.write(new File(d, fn).toPath(), data);
                File base = new File(getServletContext().getRealPath("/")); File cur = base; File project=null; for(int i=0;i<10&&cur!=null;i++){ File w=new File(cur,"web"); if(w.exists()){ project=cur; break;} cur=cur.getParentFile(); }
                if(project==null){ File u=new File(System.getProperty("user.dir")); if(new File(u,"web").exists()) project=u; }
                if(project!=null){ File pd=new File(project,"web/animalsPicture"); if(!pd.exists()) pd.mkdirs(); java.nio.file.Files.write(new File(pd, fn).toPath(), data); }
                photoPath = "animalsPicture/"+fn;
            }
            String rescueTime = req.getParameter("rescue_time");
            long subId = new AnimalSubmissionDao().create(userId,
                    name, species, req.getParameter("breed"), age,
                    req.getParameter("health_status"), req.getParameter("rescue_location"), rescueTime,
                    photoPath != null ? photoPath : req.getParameter("photo_url"), req.getParameter("description"));
            if (subId > 0) {
                req.setAttribute("msg", "已提交，等待管理员审核");
            } else {
                req.setAttribute("msg", "保存失败，请稍后重试");
            }
            req.getRequestDispatcher("/WEB-INF/views/user/add_animal.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
