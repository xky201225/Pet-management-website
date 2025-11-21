package dao;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AnimalDao {
    public List<Map<String, Object>> listAll() throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id,name,species,breed,age,health_status,photo_url,adopted FROM animals ORDER BY created_at DESC")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("name", rs.getString("name"));
                    m.put("species", rs.getString("species"));
                    m.put("breed", rs.getString("breed"));
                    m.put("age", rs.getInt("age"));
                    m.put("health_status", rs.getString("health_status"));
                    m.put("photo_url", rs.getString("photo_url"));
                    m.put("adopted", rs.getBoolean("adopted"));
                    list.add(m);
                }
            }
        }
        return list;
    }

    public Map<String, Object> getById(long id) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM animals WHERE id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();
                    for (int i = 1; i <= cols; i++) {
                        m.put(md.getColumnLabel(i), rs.getObject(i));
                    }
                    return m;
                }
            }
        }
        return null;
    }
}

