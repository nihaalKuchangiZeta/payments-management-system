package dao;

import model.User;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public User getUserByUsername(String username) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT username, password, role FROM users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String uname = rs.getString("username");
            String hashedPassword = rs.getString("password");
            String role = rs.getString("role");
            return new User(uname, hashedPassword, role);
        }

        return null; // user not found
    }

    public boolean insertUser(User user) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, user.getUsername());
        stmt.setString(2, user.getHashedPassword());
        stmt.setString(3, user.getRole());

        try {
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            // Username already exists (primary key violation)
            return false;
        }
    }

    public boolean updatePassword(String username, String newHashedPassword) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, newHashedPassword);
        stmt.setString(2, username);

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public boolean deleteUser(String username) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public List<User> getAllUsers() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT username, role FROM users ORDER BY role";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<User> users = new ArrayList<>();
        while (rs.next()) {
            String uname = rs.getString("username");
            String role = rs.getString("role");
            // No need to load password for view
            users.add(new User(uname, null, role));
        }

        return users;
    }
}
