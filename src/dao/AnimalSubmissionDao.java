package dao;

import util.DBUtil;

import java.sql.*;

public class AnimalSubmissionDao {
    public long create(long userId, String name, String species, String breed, int age, String health, String rescueLoc, String rescueTime, String photoUrl, String description) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO animal_submissions(user_id,name,species,breed,age,health_status,rescue_location,rescue_time,photo_url,description,status) VALUES(?,?,?,?,?,?,?,?,?,?,'未批准')", Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setString(2, name);
            ps.setString(3, species);
            ps.setString(4, breed);
            ps.setInt(5, age);
            ps.setString(6, health);
            ps.setString(7, rescueLoc);
            ps.setString(8, rescueTime);
            ps.setString(9, photoUrl);
            ps.setString(10, description);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            return 0L;
        }
    }

    public java.util.List<java.util.Map<String,Object>> listByUser(long userId) throws SQLException {
        java.util.List<java.util.Map<String,Object>> list = new java.util.ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id,name,species,status,`check`,created_at FROM animal_submissions WHERE user_id=? ORDER BY created_at DESC")) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String,Object> m = new java.util.HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("name", rs.getString("name"));
                    m.put("species", rs.getString("species"));
                    m.put("status", rs.getString("status"));
                    m.put("check", rs.getString("check"));
                    m.put("created_at", rs.getTimestamp("created_at"));
                    list.add(m);
                }
            }
        }
        return list;
    }

    public java.util.Map<String,Object> getById(long id) throws SQLException {
        try (java.sql.Connection conn = DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement("SELECT id,user_id,name,species,breed,age,health_status,rescue_location,rescue_time,photo_url,description FROM animal_submissions WHERE id=?")) {
            ps.setLong(1, id);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.util.Map<String,Object> m = new java.util.HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("user_id", rs.getLong("user_id"));
                    m.put("name", rs.getString("name"));
                    m.put("species", rs.getString("species"));
                    m.put("breed", rs.getString("breed"));
                    m.put("age", rs.getInt("age"));
                    m.put("health_status", rs.getString("health_status"));
                    m.put("rescue_location", rs.getString("rescue_location"));
                    m.put("rescue_time", rs.getString("rescue_time"));
                    m.put("photo_url", rs.getString("photo_url"));
                    m.put("description", rs.getString("description"));
                    return m;
                }
            }
        }
        return null;
    }
}

