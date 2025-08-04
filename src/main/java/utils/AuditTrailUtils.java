package utils;

import dao.AuditTrailDao;
import model.User;
import org.json.JSONObject;

import java.sql.SQLException;

public class AuditTrailUtils {

    private static final AuditTrailDao auditTrailDao = new AuditTrailDao();

    public static void logAction(String action, JSONObject additionalData) throws SQLException {
        User user = SessionContext.getCurrentUser();
        JSONObject json = new JSONObject();
        json.put("USER", user.getUsername());
        json.put("ROLE", user.getRole());
        json.put("ACTION", action);

        if (additionalData != null) {
            for (String key : additionalData.keySet()) {
                json.put(key, additionalData.get(key));
            }
        }

        try {
            auditTrailDao.insertAudit(json);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Overload for simple usage
    public static void logAction(String action) throws SQLException {
        logAction(action, null);
    }
}
