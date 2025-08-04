package dao;

import model.Payment;
import utils.DBUtil;
import utils.PaymentIdGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao {

    public boolean insertPayment(Payment payment) {
        String pid = PaymentIdGenerator.generate(payment.getCategory(), payment.getType());
        payment.setPid(pid);

        String sql = "INSERT INTO Payments (PID, type, category, counter_party_id, amount, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getPid());
            stmt.setString(2, payment.getType());
            stmt.setString(3, payment.getCategory());
            stmt.setString(4, payment.getCounterPartyId());
            stmt.setLong(5, payment.getAmount());
            stmt.setString(6, payment.getPaymentStatus());

            stmt.executeUpdate();
            System.out.println("✅ Payment inserted with PID: " + payment.getPid());
            return true;

        } catch (SQLException e) {
            System.err.println("❌ Failed to insert payment: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE Payments SET type = ?, category = ?, counter_party_id = ?, amount = ?, payment_status = ? WHERE PID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, payment.getType());
            stmt.setString(2, payment.getCategory());
            stmt.setString(3, payment.getCounterPartyId());
            stmt.setLong(4, payment.getAmount());
            stmt.setString(5, payment.getPaymentStatus());
            stmt.setString(6, payment.getPid());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Failed to update payment: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePayment(String pid) {
        String sql = "DELETE FROM Payments WHERE PID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pid);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Failed to delete payment: " + e.getMessage());
            return false;
        }
    }

    public Payment getPaymentById(String pid) {
        String sql = "SELECT * FROM Payments WHERE PID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch payment by ID: " + e.getMessage());
        }

        return null;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch all payments: " + e.getMessage());
        }

        return payments;
    }

    public List<Payment> getPaymentsByCategory(String category) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE category = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch payments by category: " + e.getMessage());
        }

        return payments;
    }

    public List<Payment> getPaymentsByStatus(String status) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE payment_status = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch payments by status: " + e.getMessage());
        }

        return payments;
    }

    public List<Payment> getPaymentsByDateRange(String startDate, String endDate) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE DATE(timestamp) BETWEEN ? AND ? ORDER BY timestamp";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to fetch payments by date range: " + e.getMessage());
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
        p.setPid(rs.getString("PID"));
        return p;
    }
}