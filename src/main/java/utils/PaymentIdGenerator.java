package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

public class PaymentIdGenerator {

    public static String generate(String category, String type) {
        String prefix = "PAY";
        String datePart = new SimpleDateFormat("MMyy").format(new Date());

        String catCode = getCategoryCode(category);
        String typeCode = getTypeCode(type);
        int seqNo = getNextSequenceNumber(prefix + datePart + typeCode + catCode);

        return String.format("%s%s%s%s%04d", prefix, datePart, typeCode, catCode, seqNo);
    }

    private static String getCategoryCode(String category) {
        switch (category.toLowerCase()) {
            case "salary": return "SAL";
            case "vendor payment": return "VND";
            case "client invoice": return "CLI";
            default: return "OTH";
        }
    }

    private static String getTypeCode(String type) {
        return type.equalsIgnoreCase("Incoming") ? "IN" : "OU";
    }

    private static int getNextSequenceNumber(String pidPrefix) {
        // PostgreSQL-optimized query to get the maximum sequence number
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(PID FROM LENGTH(PID) - 3) AS INTEGER)), 0) + 1 " +
                "FROM Payments WHERE PID LIKE ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pidPrefix + "%");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }
}