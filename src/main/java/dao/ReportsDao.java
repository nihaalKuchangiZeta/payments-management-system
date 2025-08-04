package dao;

import model.Payment;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsDao {

    // Monthly Summary - Get total incoming and outgoing amounts for a specific month/year
    public Map<String, Long> getMonthlySummary(String monthYear) {
        Map<String, Long> summary = new HashMap<>();
        String sql = "SELECT type, SUM(amount) as total FROM Payments " +
                "WHERE TO_CHAR(timestamp, 'MM-YY') = ? " +
                "GROUP BY type";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, monthYear);
            ResultSet rs = stmt.executeQuery();

            // Initialize with 0 values
            summary.put("Incoming", 0L);
            summary.put("Outgoing", 0L);

            while (rs.next()) {
                String type = rs.getString("type");
                Long total = rs.getLong("total");
                summary.put(type, total);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch monthly summary: " + e.getMessage());
        }

        return summary;
    }

    // Get monthly transaction count
    public Map<String, Integer> getMonthlyTransactionCount(String monthYear) {
        Map<String, Integer> count = new HashMap<>();
        String sql = "SELECT type, COUNT(*) as count FROM Payments " +
                "WHERE TO_CHAR(timestamp, 'MM-YY') = ? " +
                "GROUP BY type";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, monthYear);
            ResultSet rs = stmt.executeQuery();

            // Initialize with 0 values
            count.put("Incoming", 0);
            count.put("Outgoing", 0);

            while (rs.next()) {
                String type = rs.getString("type");
                Integer transactionCount = rs.getInt("count");
                count.put(type, transactionCount);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch monthly transaction count: " + e.getMessage());
        }

        return count;
    }

    // Category-wise Breakdown - Get amount and count by category
    public Map<String, Map<String, Object>> getCategoryBreakdown() {
        Map<String, Map<String, Object>> breakdown = new HashMap<>();
        String sql = "SELECT category, type, COUNT(*) as count, SUM(amount) as total " +
                "FROM Payments GROUP BY category, type ORDER BY category";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("category");
                String type = rs.getString("type");
                Integer count = rs.getInt("count");
                Long total = rs.getLong("total");

                breakdown.computeIfAbsent(category, k -> new HashMap<>());
                Map<String, Object> categoryData = breakdown.get(category);

                categoryData.put(type + "_count", count);
                categoryData.put(type + "_total", total);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch category breakdown: " + e.getMessage());
        }

        return breakdown;
    }

    // Payment Status Summary - Get count and total amount by status
    public Map<String, Map<String, Object>> getPaymentStatusSummary() {
        Map<String, Map<String, Object>> statusSummary = new HashMap<>();
        String sql = "SELECT payment_status, COUNT(*) as count, SUM(amount) as total " +
                "FROM Payments GROUP BY payment_status ORDER BY payment_status";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("payment_status");
                Integer count = rs.getInt("count");
                Long total = rs.getLong("total");

                Map<String, Object> statusData = new HashMap<>();
                statusData.put("count", count);
                statusData.put("total", total);

                statusSummary.put(status, statusData);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch payment status summary: " + e.getMessage());
        }

        return statusSummary;
    }

    // Get top categories by amount (for insights)
    public List<Map<String, Object>> getTopCategoriesByAmount(int limit) {
        List<Map<String, Object>> topCategories = new ArrayList<>();
        String sql = "SELECT category, COUNT(*) as count, SUM(amount) as total " +
                "FROM Payments GROUP BY category ORDER BY total DESC LIMIT ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> categoryInfo = new HashMap<>();
                categoryInfo.put("category", rs.getString("category"));
                categoryInfo.put("count", rs.getInt("count"));
                categoryInfo.put("total", rs.getLong("total"));
                topCategories.add(categoryInfo);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch top categories: " + e.getMessage());
        }

        return topCategories;
    }

    // Get payments by date range for detailed reporting
    public List<Payment> getPaymentsForDateRange(String startDate, String endDate) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE DATE(timestamp) BETWEEN ? AND ? ORDER BY timestamp DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch payments for date range: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid date format provided: " + e.getMessage());
        }

        return payments;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment(
                rs.getString("type"),
                rs.getString("category"),
                rs.getString("counter_party_id"),
                rs.getLong("amount"),
                rs.getString("payment_status")
        );
        p.setPid(rs.getString("pid"));
        return p;
    }
}