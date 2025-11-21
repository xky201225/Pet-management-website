package dao;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdoptionDao {
    public long create(long userId, long animalId, String residence, String experience, String message) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO adoption_applications(user_id,animal_id,residence,experience,message) VALUES(?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setLong(2, animalId);
            ps.setString(3, residence);
            ps.setString(4, experience);
            ps.setString(5, message);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            return 0L;
        }
    }

    public List<Map<String, Object>> listByUser(long userId) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT a.id,a.animal_id,a.status,a.review_opinion,a.`check`,a.created_at,an.name AS animal_name FROM adoption_applications a JOIN animals an ON a.animal_id=an.id WHERE a.user_id=? ORDER BY a.created_at DESC")) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("animal_id", rs.getLong("animal_id"));
                    m.put("status", rs.getString("status"));
                    m.put("review_opinion", rs.getString("review_opinion"));
                    m.put("check", rs.getString("check"));
                    m.put("created_at", rs.getTimestamp("created_at"));
                    m.put("animal_name", rs.getString("animal_name"));
                    list.add(m);
                }
            }
        }
        return list;
    }
}

