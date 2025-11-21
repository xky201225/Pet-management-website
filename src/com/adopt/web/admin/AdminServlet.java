package com.adopt.web.admin;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@MultipartConfig
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Long adminId = s == null ? null : (Long) s.getAttribute("adminId");
        if (adminId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            try (Connection conn = DBUtil.getConnection()) {
                long users = count(conn, "SELECT COUNT(*) FROM users");
                long animals = count(conn, "SELECT COUNT(*) FROM animals WHERE adopted=0");
                long appsPending = count(conn, "SELECT COUNT(*) FROM adoption_applications WHERE `check`='未处理'");
                long pendingSubs = count(conn, "SELECT COUNT(*) FROM animal_submissions WHERE `check`='未处理'");
                req.setAttribute("users", users);
                req.setAttribute("animals", animals);
                req.setAttribute("appsPending", appsPending);
                req.setAttribute("pendingSubs", pendingSubs);
                req.getRequestDispatcher("/WEB-INF/views/admin/index.jsp").forward(req, resp);
            } catch (Exception e) {
                throw new ServletException(e);
            }
            return;
        }
        try (Connection conn = DBUtil.getConnection()) {
            if ("/users".equals(path)) {
                ResultSet rs = conn.prepareStatement("SELECT id,username,phone,status,created_at FROM users ORDER BY created_at DESC").executeQuery();
                req.setAttribute("rs", rs);
                req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
                rs.close();
            } else if ("/animals".equals(path)) {
                java.util.List<java.util.Map<String,Object>> animals = new java.util.ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement("SELECT id,name,species,breed,age,health_status,adopted FROM animals ORDER BY created_at DESC"); ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        java.util.Map<String,Object> m = new java.util.HashMap<>();
                        m.put("id", rs.getLong("id"));
                        m.put("name", rs.getString("name"));
                        m.put("species", rs.getString("species"));
                        m.put("breed", rs.getString("breed"));
                        m.put("age", rs.getInt("age"));
                        m.put("health_status", rs.getString("health_status"));
                        m.put("adopted", rs.getInt("adopted"));
                        animals.add(m);
                    }
                }
                req.setAttribute("animalsList", animals);
                req.getRequestDispatcher("/WEB-INF/views/admin/animals.jsp").forward(req, resp);
            } else if ("/adoptions".equals(path)) {
                ResultSet pending = conn.prepareStatement("SELECT a.id,u.username,an.id AS animal_id,an.name,a.status,a.created_at FROM adoption_applications a JOIN users u ON a.user_id=u.id JOIN animals an ON a.animal_id=an.id WHERE a.`check`='未处理' ORDER BY a.created_at DESC").executeQuery();
                ResultSet done = conn.prepareStatement("SELECT a.id,u.username,an.id AS animal_id,an.name,a.status,a.created_at FROM adoption_applications a JOIN users u ON a.user_id=u.id JOIN animals an ON a.animal_id=an.id WHERE a.`check`='已处理' ORDER BY a.created_at DESC").executeQuery();
                req.setAttribute("pending", pending);
                req.setAttribute("done", done);
                req.getRequestDispatcher("/WEB-INF/views/admin/adoptions.jsp").forward(req, resp);
                pending.close();
                done.close();
            } else if ("/review".equals(path)) {
                java.util.List<java.util.Map<String,Object>> subsList = new java.util.ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement("SELECT s.id,s.user_id,u.username,s.name,s.species,s.breed,s.age,s.health_status,s.rescue_location,s.rescue_time,s.photo_url,s.description,s.status,s.`check` FROM animal_submissions s JOIN users u ON s.user_id=u.id WHERE s.`check`='未处理' ORDER BY s.created_at DESC"); ResultSet subs = ps.executeQuery()) {
                    while (subs.next()) {
                        java.util.Map<String,Object> m = new java.util.HashMap<>();
                        m.put("id", subs.getLong("id"));
                        m.put("user_id", subs.getLong("user_id"));
                        m.put("username", subs.getString("username"));
                        m.put("name", subs.getString("name"));
                        m.put("species", subs.getString("species"));
                        m.put("breed", subs.getString("breed"));
                        m.put("age", subs.getInt("age"));
                        m.put("health_status", subs.getString("health_status"));
                        m.put("rescue_location", subs.getString("rescue_location"));
                        m.put("rescue_time", subs.getString("rescue_time"));
                        m.put("photo_url", subs.getString("photo_url"));
                        m.put("description", subs.getString("description"));
                        m.put("status", subs.getString("status"));
                        subsList.add(m);
                    }
                }
                java.util.List<java.util.Map<String,Object>> pendingApps = new java.util.ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement("SELECT a.id,u.username,an.id AS animal_id,an.name AS animal_name,a.residence,a.experience,a.message,a.created_at FROM adoption_applications a JOIN users u ON a.user_id=u.id JOIN animals an ON a.animal_id=an.id WHERE a.`check`='未处理' ORDER BY a.created_at DESC"); ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        java.util.Map<String,Object> m = new java.util.HashMap<>();
                        m.put("id", rs.getLong("id"));
                        m.put("username", rs.getString("username"));
                        m.put("animal_id", rs.getLong("animal_id"));
                        m.put("animal_name", rs.getString("animal_name"));
                        m.put("residence", rs.getString("residence"));
                        m.put("experience", rs.getString("experience"));
                        m.put("message", rs.getString("message"));
                        m.put("created_at", rs.getTimestamp("created_at"));
                        pendingApps.add(m);
                    }
                }
                req.setAttribute("subsList", subsList);
                req.setAttribute("pendingApps", pendingApps);
                req.getRequestDispatcher("/WEB-INF/views/admin/review.jsp").forward(req, resp);
            } else if ("/forum".equals(path)) {
                ResultSet rs = conn.prepareStatement("SELECT id,title,category,status,created_at FROM topics ORDER BY created_at DESC").executeQuery();
                req.setAttribute("rs", rs);
                req.getRequestDispatcher("/WEB-INF/views/admin/forum.jsp").forward(req, resp);
                rs.close();
            } else if ("/forum/detail".equals(path)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("SELECT t.id,t.title,t.content,t.category,t.status,t.created_at,u.username,u.avatar_url,(SELECT COUNT(*) FROM likes l WHERE l.topic_id=t.id) AS likes_count,(SELECT COUNT(*) FROM comments c WHERE c.topic_id=t.id AND c.deleted=0) AS comments_count FROM topics t JOIN users u ON t.user_id=u.id WHERE t.id=?");
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                java.util.Map<String,Object> t = null;
                if (rs.next()) {
                    t = new java.util.HashMap<>();
                    t.put("id", rs.getLong("id"));
                    t.put("title", rs.getString("title"));
                    t.put("content", rs.getString("content"));
                    t.put("category", rs.getString("category"));
                    t.put("status", rs.getString("status"));
                    t.put("created_at", rs.getTimestamp("created_at"));
                    t.put("username", rs.getString("username"));
                    t.put("avatar_url", rs.getString("avatar_url"));
                    t.put("likes_count", rs.getInt("likes_count"));
                    t.put("comments_count", rs.getInt("comments_count"));
                }
                rs.close(); ps.close();
                req.setAttribute("topic", t);
                req.getRequestDispatcher("/WEB-INF/views/admin/forum_detail.jsp").forward(req, resp);
            } else if ("/knowledge".equals(path)) {
                ResultSet rs = conn.prepareStatement("SELECT id,title,author,category,status,published_at FROM knowledge_articles ORDER BY published_at DESC").executeQuery();
                req.setAttribute("rs", rs);
                req.getRequestDispatcher("/WEB-INF/views/admin/knowledge.jsp").forward(req, resp);
                rs.close();
            } else if ("/knowledge/detail".equals(path)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("SELECT ka.id,ka.title,ka.content,ka.author,ka.category,ka.status,ka.published_at,u.username,u.avatar_url,(SELECT COUNT(*) FROM article_likes al WHERE al.article_id=ka.id) AS likes_count,(SELECT COUNT(*) FROM article_comments ac WHERE ac.article_id=ka.id) AS comments_count FROM knowledge_articles ka JOIN users u ON ka.user_id=u.id WHERE ka.id=?");
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                java.util.Map<String,Object> a = null;
                if (rs.next()) {
                    a = new java.util.HashMap<>();
                    a.put("id", rs.getLong("id"));
                    a.put("title", rs.getString("title"));
                    a.put("content", rs.getString("content"));
                    a.put("author", rs.getString("author"));
                    a.put("category", rs.getString("category"));
                    a.put("status", rs.getString("status"));
                    a.put("published_at", rs.getTimestamp("published_at"));
                    a.put("username", rs.getString("username"));
                    a.put("avatar_url", rs.getString("avatar_url"));
                    a.put("likes_count", rs.getInt("likes_count"));
                    a.put("comments_count", rs.getInt("comments_count"));
                }
                rs.close(); ps.close();
                req.setAttribute("article", a);
                req.getRequestDispatcher("/WEB-INF/views/admin/knowledge_detail.jsp").forward(req, resp);
            } else if ("/user".equals(path)) {
                long uid = Long.parseLong(req.getParameter("id"));
                java.util.Map<String,Object> user = new java.util.HashMap<>();
                PreparedStatement p1 = conn.prepareStatement("SELECT id,username,avatar_url,phone,created_at FROM users WHERE id=?");
                p1.setLong(1, uid);
                try (ResultSet r1 = p1.executeQuery()) {
                    if (r1.next()) {
                        user.put("id", r1.getLong("id"));
                        user.put("username", r1.getString("username"));
                        user.put("avatar_url", r1.getString("avatar_url"));
                        user.put("phone", r1.getString("phone"));
                        user.put("created_at", r1.getTimestamp("created_at"));
                    }
                }
                p1.close();
                ResultSet tp = conn.prepareStatement("SELECT id,title,created_at,status FROM topics WHERE user_id="+uid+" ORDER BY created_at DESC").executeQuery();
                ResultSet ka = conn.prepareStatement("SELECT id,title,published_at,status FROM knowledge_articles WHERE user_id="+uid+" ORDER BY published_at DESC").executeQuery();
                req.setAttribute("user", user);
                req.setAttribute("userTopics", tp);
                req.setAttribute("userArticles", ka);
                req.getRequestDispatcher("/WEB-INF/views/admin/user_detail.jsp").forward(req, resp);
                tp.close(); ka.close();
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private long count(Connection conn, String sql) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
            return 0;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        Long adminId = s == null ? null : (Long) s.getAttribute("adminId");
        if (adminId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String action = req.getParameter("action");
        try (Connection conn = DBUtil.getConnection()) {
            if ("banUser".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("UPDATE users SET status='banned' WHERE id=?");
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                resp.sendRedirect(req.getContextPath() + "/admin/users");
            } else if ("unbanUser".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("UPDATE users SET status='active' WHERE id=?");
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                resp.sendRedirect(req.getContextPath() + "/admin/users");
            } else if ("addAnimal".equals(action)) {
                String dir = getServletContext().getRealPath("/animalsPicture");
                File d = new File(dir); if (!d.exists()) d.mkdirs();
                Part photo = null; try { photo = req.getPart("photo_file"); } catch (Exception ignore) {}
                String photoPath = null;
                if (photo != null && photo.getSize() > 0) {
                    String original = photo.getSubmittedFileName();
                    String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.')) : ".png";
                    String fn = "animal_"+System.currentTimeMillis()+ext;
                    java.io.ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
                    try (java.io.InputStream in = photo.getInputStream()) { byte[] tmp = new byte[8192]; int n; while((n=in.read(tmp))>0) buf.write(tmp,0,n);} byte[] data = buf.toByteArray();
                    java.nio.file.Files.write(new File(d, fn).toPath(), data);
                    File base = new File(getServletContext().getRealPath("/")); File cur = base; File project=null; for(int i=0;i<10&&cur!=null;i++){ File w=new File(cur,"web"); if(w.exists()){ project=cur; break;} cur=cur.getParentFile(); }
                    if(project==null){ File u=new File(System.getProperty("user.dir")); if(new File(u,"web").exists()) project=u; }
                    if(project!=null){ File pd=new File(project,"web/animalsPicture"); if(!pd.exists()) pd.mkdirs(); java.nio.file.Files.write(new File(pd, fn).toPath(), data); }
                    photoPath = "animalsPicture/"+fn;
                }
                PreparedStatement ps = conn.prepareStatement("INSERT INTO animals(name,species,breed,age,health_status,rescue_location,rescue_time,photo_url,description) VALUES(?,?,?,?,?,?,?,?,?)");
                String name = req.getParameter("name");
                String species = req.getParameter("species");
                String ageStr = req.getParameter("age");
                if (name == null || name.isEmpty() || species == null || species.isEmpty() || ageStr == null || ageStr.isEmpty()) {
                    req.setAttribute("error", "请完整填写必填项");
                    java.util.List<java.util.Map<String,Object>> animals = new java.util.ArrayList<>();
                    try (PreparedStatement p1 = conn.prepareStatement("SELECT id,name,species,breed,age,health_status,adopted FROM animals ORDER BY created_at DESC"); ResultSet r1 = p1.executeQuery()) {
                        while (r1.next()) {
                            java.util.Map<String,Object> m = new java.util.HashMap<>();
                            m.put("id", r1.getLong("id")); m.put("name", r1.getString("name")); m.put("species", r1.getString("species")); m.put("breed", r1.getString("breed")); m.put("age", r1.getInt("age")); m.put("health_status", r1.getString("health_status")); m.put("adopted", r1.getInt("adopted"));
                            animals.add(m);
                        }
                    }
                    java.util.List<java.util.Map<String,Object>> subsList = new java.util.ArrayList<>();
                    try (PreparedStatement p2 = conn.prepareStatement("SELECT s.id,s.user_id,u.username,s.name,s.species,s.breed,s.age,s.health_status,s.rescue_location,s.rescue_time,s.photo_url,s.description,s.status FROM animal_submissions s JOIN users u ON s.user_id=u.id WHERE s.status='未批准' ORDER BY s.created_at DESC"); ResultSet r2 = p2.executeQuery()) {
                        while (r2.next()) {
                            java.util.Map<String,Object> m = new java.util.HashMap<>();
                            m.put("id", r2.getLong("id")); m.put("user_id", r2.getLong("user_id")); m.put("username", r2.getString("username")); m.put("name", r2.getString("name")); m.put("species", r2.getString("species")); m.put("breed", r2.getString("breed")); m.put("age", r2.getInt("age")); m.put("health_status", r2.getString("health_status")); m.put("rescue_location", r2.getString("rescue_location")); m.put("rescue_time", r2.getString("rescue_time")); m.put("photo_url", r2.getString("photo_url")); m.put("description", r2.getString("description")); m.put("status", r2.getString("status"));
                            subsList.add(m);
                        }
                    }
                    req.setAttribute("animalsList", animals);
                    req.setAttribute("subsList", subsList);
                    req.getRequestDispatcher("/WEB-INF/views/admin/animals.jsp").forward(req, resp);
                    return;
                }
                int age;
                try { age = Integer.parseInt(ageStr); } catch (NumberFormatException e) {
                    req.setAttribute("error", "年龄必须是数字");
                    java.util.List<java.util.Map<String,Object>> animals = new java.util.ArrayList<>();
                    try (PreparedStatement p1 = conn.prepareStatement("SELECT id,name,species,breed,age,health_status,adopted FROM animals ORDER BY created_at DESC"); ResultSet r1 = p1.executeQuery()) {
                        while (r1.next()) {
                            java.util.Map<String,Object> m = new java.util.HashMap<>();
                            m.put("id", r1.getLong("id")); m.put("name", r1.getString("name")); m.put("species", r1.getString("species")); m.put("breed", r1.getString("breed")); m.put("age", r1.getInt("age")); m.put("health_status", r1.getString("health_status")); m.put("adopted", r1.getInt("adopted"));
                            animals.add(m);
                        }
                    }
                    java.util.List<java.util.Map<String,Object>> subsList = new java.util.ArrayList<>();
                    try (PreparedStatement p2 = conn.prepareStatement("SELECT s.id,s.user_id,u.username,s.name,s.species,s.breed,s.age,s.health_status,s.rescue_location,s.rescue_time,s.photo_url,s.description,s.status FROM animal_submissions s JOIN users u ON s.user_id=u.id WHERE s.status='未批准' ORDER BY s.created_at DESC"); ResultSet r2 = p2.executeQuery()) {
                        while (r2.next()) {
                            java.util.Map<String,Object> m = new java.util.HashMap<>();
                            m.put("id", r2.getLong("id")); m.put("user_id", r2.getLong("user_id")); m.put("username", r2.getString("username")); m.put("name", r2.getString("name")); m.put("species", r2.getString("species")); m.put("breed", r2.getString("breed")); m.put("age", r2.getInt("age")); m.put("health_status", r2.getString("health_status")); m.put("rescue_location", r2.getString("rescue_location")); m.put("rescue_time", r2.getString("rescue_time")); m.put("photo_url", r2.getString("photo_url")); m.put("description", r2.getString("description")); m.put("status", r2.getString("status"));
                            subsList.add(m);
                        }
                    }
                    req.setAttribute("animalsList", animals);
                    req.setAttribute("subsList", subsList);
                    req.getRequestDispatcher("/WEB-INF/views/admin/animals.jsp").forward(req, resp);
                    return;
                }
                ps.setString(1, name);
                ps.setString(2, species);
                ps.setString(3, req.getParameter("breed"));
                ps.setInt(4, age);
                ps.setString(5, req.getParameter("health_status"));
                ps.setString(6, req.getParameter("rescue_location"));
                String rescueTime = req.getParameter("rescue_time");
                try {
                    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd").withResolverStyle(java.time.format.ResolverStyle.STRICT);
                    java.time.LocalDate ld = java.time.LocalDate.parse(rescueTime, fmt);
                    java.sql.Timestamp ts = java.sql.Timestamp.valueOf(ld.atStartOfDay());
                    ps.setTimestamp(7, ts);
                } catch (Exception pe) {
                    // 若解析失败，使用当前日期作为兜底，避免页面中断
                    java.time.LocalDate ld = java.time.LocalDate.now();
                    java.sql.Timestamp ts = java.sql.Timestamp.valueOf(ld.atStartOfDay());
                    ps.setTimestamp(7, ts);
                }
                ps.setString(8, photoPath != null ? photoPath : req.getParameter("photo_url"));
                ps.setString(9, req.getParameter("description"));
                ps.executeUpdate();
                ps.close();
                resp.sendRedirect(req.getContextPath() + "/admin/animals");
            } else if ("updateAnimal".equals(action)) {
                String xhr = req.getHeader("X-Requested-With");
                String health = req.getParameter("health_status");
                String adoptedStr = req.getParameter("adopted");
                String idStr = req.getParameter("id");
                boolean invalid = false;
                Integer adoptedVal = null;
                Long idVal = null;
                if (health == null || health.trim().isEmpty()) invalid = true;
                try { adoptedVal = Integer.parseInt(adoptedStr); } catch (Exception e) { invalid = true; }
                try { idVal = Long.parseLong(idStr); } catch (Exception e) { invalid = true; }
                if (invalid) {
                    if (xhr != null && xhr.equals("XMLHttpRequest")) {
                        resp.setContentType("application/json;charset=UTF-8");
                        resp.setStatus(400);
                        resp.getWriter().write("{\"status\":\"error\",\"message\":\"请完整填写必填项\"}");
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/admin/animals?error=请完整填写必填项");
                    }
                } else {
                    PreparedStatement ps = conn.prepareStatement("UPDATE animals SET health_status=?, adopted=? WHERE id=?");
                    ps.setString(1, health);
                    ps.setInt(2, adoptedVal);
                    ps.setLong(3, idVal);
                    int n = ps.executeUpdate();
                    ps.close();
                    if (xhr != null && xhr.equals("XMLHttpRequest")) {
                        resp.setContentType("application/json;charset=UTF-8");
                        if (n > 0) {
                            resp.getWriter().write("{\"status\":\"ok\"}");
                        } else {
                            resp.setStatus(404);
                            resp.getWriter().write("{\"status\":\"error\",\"message\":\"动物不存在\"}");
                        }
                    } else {
                        if (n > 0) {
                            resp.sendRedirect(req.getContextPath() + "/admin/animals?ok=1");
                        } else {
                            resp.sendRedirect(req.getContextPath() + "/admin/animals?error=动物不存在");
                        }
                    }
                }
            } else if ("deleteAnimal".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                // 删除依赖的收养申请记录后再删除动物
                PreparedStatement dApps = conn.prepareStatement("DELETE FROM adoption_applications WHERE animal_id=?");
                dApps.setLong(1, id);
                dApps.executeUpdate();
                dApps.close();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM animals WHERE id=?");
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                resp.sendRedirect(req.getContextPath() + "/admin/animals");
            } else if ("approveApp".equals(action) || "rejectApp".equals(action)) {
                long appId = Long.parseLong(req.getParameter("id"));
                String status = "approveApp".equals(action) ? "批准" : "未批准";
                PreparedStatement ps = conn.prepareStatement("UPDATE adoption_applications SET status=?, review_opinion=?, `check`='已处理' WHERE id=?");
                ps.setString(1, status);
                ps.setString(2, req.getParameter("opinion"));
                ps.setLong(3, appId);
                ps.executeUpdate();
                ps.close();
                if ("approveApp".equals(action)) {
                    PreparedStatement ps2 = conn.prepareStatement("SELECT animal_id FROM adoption_applications WHERE id=?");
                    ps2.setLong(1, appId);
                    ResultSet rs = ps2.executeQuery();
                    if (rs.next()) {
                        long animalId = rs.getLong(1);
                        PreparedStatement ps3 = conn.prepareStatement("UPDATE animals SET adopted=1 WHERE id=?");
                        ps3.setLong(1, animalId);
                        ps3.executeUpdate();
                        ps3.close();
                        PreparedStatement ps4 = conn.prepareStatement("UPDATE adoption_applications SET status='未批准', `check`='已处理' WHERE animal_id=? AND id<>? AND `check`='未处理'");
                        ps4.setLong(1, animalId);
                        ps4.setLong(2, appId);
                        ps4.executeUpdate();
                        ps4.close();
                    }
                    rs.close();
                    ps2.close();
                }
                resp.sendRedirect(req.getContextPath() + "/admin/adoptions");
            } else if ("hideTopic".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("UPDATE topics SET status='hidden' WHERE id=?");
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                String ret = req.getParameter("returnDetail");
                if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath()+"/admin/forum/detail?id="+id); } else { resp.sendRedirect(req.getContextPath()+"/admin/forum"); }
            } else if ("showTopic".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("UPDATE topics SET status='visible' WHERE id=?");
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                String ret = req.getParameter("returnDetail");
                if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath()+"/admin/forum/detail?id="+id); } else { resp.sendRedirect(req.getContextPath()+"/admin/forum"); }
            } else if ("deleteTopic".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement d1 = conn.prepareStatement("DELETE FROM comments WHERE topic_id=?");
                d1.setLong(1, id); d1.executeUpdate(); d1.close();
                PreparedStatement d2 = conn.prepareStatement("DELETE FROM likes WHERE topic_id=?");
                d2.setLong(1, id); d2.executeUpdate(); d2.close();
                PreparedStatement d3 = conn.prepareStatement("DELETE FROM topics WHERE id=?");
                d3.setLong(1, id); d3.executeUpdate(); d3.close();
                String ret = req.getParameter("returnDetail");
                if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath()+"/admin/forum"); } else { resp.sendRedirect(req.getContextPath()+"/admin/forum"); }
            } else if ("deleteComment".equals(action)) {
                PreparedStatement ps = conn.prepareStatement("UPDATE comments SET deleted=1 WHERE id=?");
                ps.setLong(1, Long.parseLong(req.getParameter("id")));
                ps.executeUpdate();
                ps.close();
                resp.sendRedirect(req.getContextPath() + "/admin/forum");
            } else if ("hideArticle".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("UPDATE knowledge_articles SET status='hidden' WHERE id=?");
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                String ret = req.getParameter("returnDetail");
                if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath()+"/admin/knowledge/detail?id="+id); } else { resp.sendRedirect(req.getContextPath()+"/admin/knowledge"); }
            } else if ("showArticle".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("UPDATE knowledge_articles SET status='visible' WHERE id=?");
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                String ret = req.getParameter("returnDetail");
                if ("1".equals(ret)) { resp.sendRedirect(req.getContextPath()+"/admin/knowledge/detail?id="+id); } else { resp.sendRedirect(req.getContextPath()+"/admin/knowledge"); }
            } else if ("deleteArticle".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement d1 = conn.prepareStatement("DELETE FROM article_comments WHERE article_id=?");
                d1.setLong(1, id); d1.executeUpdate(); d1.close();
                PreparedStatement d2 = conn.prepareStatement("DELETE FROM article_likes WHERE article_id=?");
                d2.setLong(1, id); d2.executeUpdate(); d2.close();
                PreparedStatement d3 = conn.prepareStatement("DELETE FROM knowledge_articles WHERE id=?");
                d3.setLong(1, id); d3.executeUpdate(); d3.close();
                resp.sendRedirect(req.getContextPath() + "/admin/knowledge");
            } else if ("approveSubmission".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement ps = conn.prepareStatement("SELECT name,species,breed,age,health_status,rescue_location,rescue_time,photo_url,description FROM animal_submissions WHERE id=?");
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    PreparedStatement ins = conn.prepareStatement("INSERT INTO animals(name,species,breed,age,health_status,rescue_location,rescue_time,photo_url,description) VALUES(?,?,?,?,?,?,?,?,?)");
                    ins.setString(1, rs.getString(1));
                    ins.setString(2, rs.getString(2));
                    ins.setString(3, rs.getString(3));
                    ins.setInt(4, rs.getInt(4));
                    ins.setString(5, rs.getString(5));
                    ins.setString(6, rs.getString(6));
                    ins.setString(7, rs.getString(7));
                    ins.setString(8, rs.getString(8));
                    ins.setString(9, rs.getString(9));
                    ins.executeUpdate();
                    ins.close();
                }
                rs.close();
                ps.close();
                PreparedStatement upd = conn.prepareStatement("UPDATE animal_submissions SET status='批准', `check`='已处理' WHERE id=?");
                upd.setLong(1, id);
                upd.executeUpdate();
                upd.close();
                resp.sendRedirect(req.getContextPath() + "/admin/animals");
            } else if ("rejectSubmission".equals(action)) {
                long id = Long.parseLong(req.getParameter("id"));
                PreparedStatement upd = conn.prepareStatement("UPDATE animal_submissions SET status='未批准', `check`='已处理' WHERE id=?");
                upd.setLong(1, id);
                upd.executeUpdate();
                upd.close();
                resp.sendRedirect(req.getContextPath() + "/admin/animals");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
