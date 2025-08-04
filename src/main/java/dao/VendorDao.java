package dao;

import model.Vendor;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendorDao {

    public Vendor getVendorById(String vendorId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT vendor_id, vendor_name, status FROM Vendor WHERE vendor_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, vendorId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String id = rs.getString("vendor_id");
            String name = rs.getString("vendor_name");
            String status = rs.getString("status");
            return new Vendor(id, name, status);
        }

        return null; // vendor not found
    }

    public boolean insertVendor(Vendor vendor) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO Vendor (vendor_id, vendor_name, status) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, vendor.getVendorId());
        stmt.setString(2, vendor.getVendorName());
        stmt.setString(3, vendor.getStatus());

        try {
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            // Vendor ID already exists (primary key violation)
            return false;
        }
    }

    public boolean updateVendor(Vendor vendor) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE Vendor SET vendor_name = ?, status = ? WHERE vendor_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, vendor.getVendorName());
        stmt.setString(2, vendor.getStatus());
        stmt.setString(3, vendor.getVendorId());

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public boolean deleteVendor(String vendorId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM Vendor WHERE vendor_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, vendorId);

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public List<Vendor> getAllVendors() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT vendor_id, vendor_name, status FROM Vendor ORDER BY vendor_id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<Vendor> vendors = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("vendor_id");
            String name = rs.getString("vendor_name");
            String status = rs.getString("status");
            vendors.add(new Vendor(id, name, status));
        }

        return vendors;
    }

    public String getNextVendorId() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT MAX(CAST(SUBSTRING(vendor_id, 2) AS INTEGER)) as max_id FROM Vendor";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int nextId = 1;
        if (rs.next() && rs.getInt("max_id") > 0) {
            nextId = rs.getInt("max_id") + 1;
        }

        return String.format("V%04d", nextId);
    }
}