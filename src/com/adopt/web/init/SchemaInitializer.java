package com.adopt.web.init;

import util.DBUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebListener
public class SchemaInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection conn = DBUtil.getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS article_likes (\n" +
                        "  id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "  article_id BIGINT NOT NULL,\n" +
                        "  user_id BIGINT NOT NULL,\n" +
                        "  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  UNIQUE KEY uk_al (article_id,user_id)\n" +
                        ") ENGINE=InnoDB");
                st.execute("CREATE TABLE IF NOT EXISTS article_comments (\n" +
                        "  id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "  article_id BIGINT NOT NULL,\n" +
                        "  user_id BIGINT NOT NULL,\n" +
                        "  content TEXT NOT NULL,\n" +
                        "  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                        ") ENGINE=InnoDB");
                st.execute("CREATE TABLE IF NOT EXISTS animal_submissions (\n" +
                        "  id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "  user_id BIGINT NOT NULL,\n" +
                        "  name VARCHAR(100) NOT NULL,\n" +
                        "  species VARCHAR(50) NOT NULL,\n" +
                        "  breed VARCHAR(100),\n" +
                        "  age INT,\n" +
                        "  health_status VARCHAR(100),\n" +
                        "  rescue_location VARCHAR(255),\n" +
                        "  rescue_time VARCHAR(64),\n" +
                        "  photo_url VARCHAR(255),\n" +
                        "  description TEXT,\n" +
                        "  status ENUM('pending','approved','rejected') NOT NULL DEFAULT 'pending',\n" +
                        "  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
                        ") ENGINE=InnoDB");
            }

            ensureColumn(conn, "admins", "invite", "ALTER TABLE admins ADD COLUMN invite VARCHAR(16)");
            ensureColumn(conn, "admins", "avatar_url", "ALTER TABLE admins ADD COLUMN avatar_url VARCHAR(255)");
            ensureColumn(conn, "knowledge_articles", "user_id", "ALTER TABLE knowledge_articles ADD COLUMN user_id BIGINT");
            ensureColumn(conn, "knowledge_articles", "status", "ALTER TABLE knowledge_articles ADD COLUMN status ENUM('visible','hidden') NOT NULL DEFAULT 'visible'");
            try (java.sql.Statement st3 = conn.createStatement()) {
                st3.execute("ALTER TABLE adoption_applications MODIFY COLUMN status ENUM('未批准','批准') NOT NULL DEFAULT '未批准'");
            } catch (Exception ignore) {}
            try (java.sql.Statement st4 = conn.createStatement()) {
                st4.execute("ALTER TABLE animal_submissions MODIFY COLUMN status ENUM('未批准','批准') NOT NULL DEFAULT '未批准'");
            } catch (Exception ignore) {}
            try (java.sql.Statement st5 = conn.createStatement()) {
                st5.execute("UPDATE adoption_applications SET status='未批准' WHERE status IN ('pending','rejected')");
                st5.execute("UPDATE adoption_applications SET status='批准' WHERE status='approved'");
                st5.execute("UPDATE animal_submissions SET status='未批准' WHERE status IN ('pending','rejected')");
                st5.execute("UPDATE animal_submissions SET status='批准' WHERE status='approved'");
            } catch (Exception ignore) {}

            try (Statement st2 = conn.createStatement()) {
                st2.execute("UPDATE admins SET invite = COALESCE(invite,'ABC123')");
            }
        } catch (Exception ignore) {}
    }

    private boolean columnExists(Connection conn, String table, String column) throws Exception {
        String sql = "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME=? AND COLUMN_NAME=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table);
            ps.setString(2, column);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1) > 0;
                return false;
            }
        }
    }

    private void ensureColumn(Connection conn, String table, String column, String alterSql) throws Exception {
        try {
            if (!columnExists(conn, table, column)) {
                try (Statement st = conn.createStatement()) { st.execute(alterSql); }
            }
        } catch (Exception ignore) {}
    }
}
