package model;

import org.json.JSONObject;
import java.sql.Timestamp;

public class AuditTrail {
    private int id;
    private String jsondb;
    private Timestamp timestamp;
    private String user;
    private String role;
    private String action;

    // Constructor with all fields
    public AuditTrail(int id, String jsondb, Timestamp timestamp) {
        this.id = id;
        this.jsondb = jsondb;
        this.timestamp = timestamp;
        extractFieldsFromJson();
    }

    // Constructor without ID (for inserts)
    public AuditTrail(String jsondb, Timestamp timestamp) {
        this.jsondb = jsondb;
        this.timestamp = timestamp;
        extractFieldsFromJson();
    }

    // Extract user, role, and action from JSON
    private void extractFieldsFromJson() {
        try {
            if (jsondb != null && !jsondb.trim().isEmpty()) {
                JSONObject json = new JSONObject(jsondb);
                this.user = json.optString("USER", "N/A");
                this.role = json.optString("ROLE", "N/A");
                this.action = json.optString("ACTION", "N/A");
            } else {
                this.user = "N/A";
                this.role = "N/A";
                this.action = "N/A";
            }
        } catch (Exception e) {
            // If JSON parsing fails, set default values
            this.user = "N/A";
            this.role = "N/A";
            this.action = "N/A";
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getJsondb() {
        return jsondb;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public String getAction() {
        return action;
    }

    // Get additional details (everything except USER, ROLE, ACTION)
    public String getAdditionalDetails() {
        try {
            if (jsondb != null && !jsondb.trim().isEmpty()) {
                JSONObject json = new JSONObject(jsondb);
                StringBuilder details = new StringBuilder();

                for (String key : json.keySet()) {
                    if (!key.equals("USER") && !key.equals("ROLE") && !key.equals("ACTION")) {
                        if (details.length() > 0) {
                            details.append(" | ");
                        }
                        details.append(key).append(": ").append(json.getString(key));
                    }
                }

                return details.toString();
            }
        } catch (Exception e) {
            // If parsing fails, return empty string
        }
        return "";
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setJsondb(String jsondb) {
        this.jsondb = jsondb;
        extractFieldsFromJson(); // Re-extract when JSON changes
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuditTrail{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", role='" + role + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}