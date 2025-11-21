package dao;

import util.DBUtil;

import java.sql.*;
import java.util.*;

public class KnowledgeDao {
    public List<Map<String, Object>> search(String category, String keyword) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT ka.id,ka.title,ka.author,ka.published_at,ka.category,ka.user_id,u.username,u.avatar_url,(SELECT COUNT(*) FROM article_likes al WHERE al.article_id=ka.id) AS likes_count,(SELECT COUNT(*) FROM article_comments ac WHERE ac.article_id=ka.id) AS comments_count FROM knowledge_articles ka JOIN users u ON ka.user_id=u.id WHERE ka.status='visible'";
        List<Object> params = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            sql += " AND ka.category=?";
            params.add(category);
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND (ka.title LIKE ? OR ka.content LIKE ?)";
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        sql += " ORDER BY ka.published_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("title", rs.getString("title"));
                    m.put("author", rs.getString("author"));
                    m.put("published_at", rs.getTimestamp("published_at"));
                    m.put("category", rs.getString("category"));
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

    public long publish(long userId, String title, String author, String category, String content) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO knowledge_articles(user_id,title,author,category,content,published_at) VALUES(?,?,?,?,?,NOW())", Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setString(4, category);
            ps.setString(5, content);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            return 0L;
        }
    }

    public Map<String, Object> getById(long id) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT ka.*, (SELECT COUNT(*) FROM article_likes al WHERE al.article_id=ka.id) AS likes_count FROM knowledge_articles ka WHERE ka.id=?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    ResultSetMetaData md = rs.getMetaData();
                    int cols = md.getColumnCount();
                    for (int i = 1; i <= cols; i++) m.put(md.getColumnLabel(i), rs.getObject(i));
                    return m;
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> listComments(long articleId) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT ac.id,u.username,ac.content,ac.created_at FROM article_comments ac JOIN users u ON ac.user_id=u.id WHERE ac.article_id=? ORDER BY ac.id ASC")) {
            ps.setLong(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getLong("id"));
                    m.put("username", rs.getString("username"));
                    m.put("content", rs.getString("content"));
                    m.put("created_at", rs.getTimestamp("created_at"));
                    list.add(m);
                }
            }
        }
        return list;
    }

    public void like(long articleId, long userId) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO article_likes(article_id,user_id) VALUES(?,?)")) {
            ps.setLong(1, articleId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void comment(long articleId, long userId, String content) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO article_comments(article_id,user_id,content) VALUES(?,?,?)")) {
            ps.setLong(1, articleId);
            ps.setLong(2, userId);
            ps.setString(3, content);
            ps.executeUpdate();
        }
    }

    public boolean delete(long articleId, Long requesterUserId, Long adminId) throws SQLException {
        try (Connection conn = DBUtil.getConnection()) {
            Long ownerId = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM knowledge_articles WHERE id=?")) {
                ps.setLong(1, articleId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) ownerId = rs.getLong(1); }
            }
            if (ownerId == null) return false;
            if (adminId == null && (requesterUserId == null || !ownerId.equals(requesterUserId))) return false;
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM knowledge_articles WHERE id=?")) {
                ps.setLong(1, articleId);
                return ps.executeUpdate() > 0;
            }
        }
    }
}
