package dao;

import model.AuditTrail;
import utils.DBUtil;

import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditTrailDao {

    public void insertAudit(JSONObject logEntry) throws SQLException {
        String sql = "INSERT INTO AuditTrails (jsondb) VALUES (?::jsonb)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, logEntry.toString());
            stmt.executeUpdate();
        }
    }

    public List<AuditTrail> getAllLogs() throws SQLException {
        String sql = "SELECT id, jsondb, timestamp FROM audittrails ORDER BY timestamp DESC";
        List<AuditTrail> auditTrails = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String jsondb = rs.getString("jsondb");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                auditTrails.add(new AuditTrail(id, jsondb, timestamp));
            }
        }

        return auditTrails;
    }

    public List<AuditTrail> getLogsByDateRange(String startDate, String endDate) throws SQLException {
        String sql = "SELECT id, jsondb, timestamp FROM audittrails " +
                "WHERE DATE(timestamp) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) " +
                "ORDER BY timestamp DESC";
        List<AuditTrail> auditTrails = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String jsondb = rs.getString("jsondb");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                auditTrails.add(new AuditTrail(id, jsondb, timestamp));
            }
        }

        return auditTrails;
    }

    public List<AuditTrail> getLogsByAction(String action) throws SQLException {
        String sql = "SELECT id, jsondb, timestamp FROM audittrails " +
                "WHERE jsondb::text LIKE ? ORDER BY timestamp DESC";
        List<AuditTrail> auditTrails = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%\"ACTION\": \"" + action + "\"%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String jsondb = rs.getString("jsondb");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                AuditTrail auditTrail = new AuditTrail(id, jsondb, timestamp);

                // Optional: further Java-side verification
                if (auditTrail.getAction().equals(action)) {
                    auditTrails.add(auditTrail);
                }
            }
        }

        return auditTrails;
    }

    public List<AuditTrail> getLogsByUser(String username) throws SQLException {
        String sql = "SELECT id, jsondb, timestamp FROM audittrails " +
                "WHERE jsondb::text LIKE ? ORDER BY timestamp DESC";
        List<AuditTrail> auditTrails = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%\"USER\": \"" + username + "\"%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String jsondb = rs.getString("jsondb");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                AuditTrail auditTrail = new AuditTrail(id, jsondb, timestamp);

                // Optional: further Java-side verification
                if (auditTrail.getUser().equals(username)) {
                    auditTrails.add(auditTrail);
                }
            }
        }

        return auditTrails;
    }


    public List<String> getDistinctActions() throws SQLException {
        String sql = "SELECT id, jsondb, timestamp FROM audittrails";
        List<String> actions = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String jsondb = rs.getString("jsondb");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                AuditTrail auditTrail = new AuditTrail(0, jsondb, timestamp);

                String action = auditTrail.getAction();
                if (!action.equals("N/A") && !actions.contains(action)) {
                    actions.add(action);
                }
            }
        }

        return actions;
    }

    public List<String> getDistinctUsers() throws SQLException {
        String sql = "SELECT id, jsondb, timestamp FROM audittrails";
        List<String> users = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String jsondb = rs.getString("jsondb");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                AuditTrail auditTrail = new AuditTrail(0, jsondb, timestamp);

                String user = auditTrail.getUser();
                if (!user.equals("N/A") && !users.contains(user)) {
                    users.add(user);
                }
            }
        }

        return users;
    }
}