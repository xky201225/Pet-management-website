package dao;

import util.DBUtil;

import java.sql.*;
import java.util.*;

public class ForumDao {
    public long createTopic(long userId, String title, String category, String content, String images) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO topics(user_id,title,category,content,images) VALUES(?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setString(2, title);
            ps.setString(3, category);
            ps.setString(4, content);
            ps.setString(5, images);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            return 0L;
        }
    }

    public List<Map<String, Object>> listTopics(String category, String keyword) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.id,t.title,t.category,t.content,t.created_at,u.id AS user_id,u.username,u.avatar_url,(SELECT COUNT(*) FROM likes l WHERE l.topic_id=t.id) AS likes_count,(SELECT COUNT(*) FROM comments c WHERE c.topic_id=t.id AND c.deleted=0) AS comments_count FROM topics t JOIN users u ON t.user_id=u.id WHERE t.status='visible'";
        List<Object> params = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            sql += " AND t.category=?";
            params.add(category);
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND (t.title LIKE ? OR t.content LIKE ?)";
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        sql += " ORDER BY t.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("title", rs.getString("title"));
                    m.put("category", rs.getString("category"));
                    m.put("content", rs.getString("content"));
                    m.put("created_at", rs.getTimestamp("created_at"));
                    m.put("user_id", rs.getLong("user_id"));
                    m.put("username", rs.getString("username"));
                    m.put("avatar_url", rs.getString("avatar_url"));
                    m.put("likes_count", rs.getInt("likes_count"));
                    m.put("comments_count", rs.getInt("comments_count"));
                list.add(m);
            }
            }
        }
        return list;
    }

    public void like(long topicId, long userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO likes(topic_id,user_id) VALUES(?,?)")) {
            ps.setLong(1, topicId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void comment(long topicId, long userId, String content) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO comments(topic_id,user_id,content) VALUES(?,?,?)")) {
            ps.setLong(1, topicId);
            ps.setLong(2, userId);
            ps.setString(3, content);
            ps.executeUpdate();
        }
    }

    public List<Map<String, Object>> listComments(long topicId) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT c.id,c.content,c.created_at,c.user_id,u.username,u.avatar_url FROM comments c JOIN users u ON c.user_id=u.id WHERE c.topic_id=? AND c.deleted=0 ORDER BY c.id ASC")) {
            ps.setLong(1, topicId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("content", rs.getString("content"));
                    m.put("created_at", rs.getTimestamp("created_at"));
                    m.put("user_id", rs.getLong("user_id"));
                    m.put("username", rs.getString("username"));
                    m.put("avatar_url", rs.getString("avatar_url"));
                    list.add(m);
                }
            }
        }
        return list;
    }
}
