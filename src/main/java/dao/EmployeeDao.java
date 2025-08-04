package dao;

import model.Employee;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    public Employee getEmployeeById(String eid) throws SQLException {
        String sql = "SELECT eid, name, department, salary FROM Employee WHERE eid = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("eid");
                String name = rs.getString("name");
                String department = rs.getString("department");
                long salary = rs.getLong("salary");
                return new Employee(id, name, department, salary);
            }

            return null; // employee not found
        }
    }

    public boolean insert(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee (eid, name, department, salary) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getEid());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getDepartment());
            stmt.setLong(4, employee.getSalary());

            try {
                int rows = stmt.executeUpdate();
                return rows > 0;
            } catch (SQLIntegrityConstraintViolationException e) {
                // Employee ID already exists (primary key violation)
                return false;
            }
        }
    }

    public boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET name = ?, department = ?, salary = ? WHERE eid = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getDepartment());
            stmt.setLong(3, employee.getSalary());
            stmt.setString(4, employee.getEid());

            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }

    public boolean delete(String eid) throws SQLException {
        String sql = "DELETE FROM Employee WHERE eid = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, eid);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM Employee ORDER BY eid ASC";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String eid = rs.getString("eid");
                String name = rs.getString("name");
                String department = rs.getString("department");
                long salary = rs.getLong("salary");

                employees.add(new Employee(eid, name, department, salary));
            }
        }

        return employees;
    }

    public String getNextEmployeeId() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT MAX(CAST(SUBSTRING(eid, 2) AS INTEGER)) as max_id FROM Employee";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int nextId = 1;
        if (rs.next() && rs.getInt("max_id") > 0) {
            nextId = rs.getInt("max_id") + 1;
        }

        return String.format("E%04d", nextId);
    }

}