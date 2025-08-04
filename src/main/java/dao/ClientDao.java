package dao;

import model.Client;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {

    public Client getClientById(String clientId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT client_id, client_name, status FROM Client WHERE client_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, clientId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String id = rs.getString("client_id");
            String name = rs.getString("client_name");
            String status = rs.getString("status");
            return new Client(id, name, status);
        }

        return null; // client not found
    }

    public boolean insertClient(Client client) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO Client (client_id, client_name, status) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, client.getClientId());
        stmt.setString(2, client.getClientName());
        stmt.setString(3, client.getStatus());

        try {
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            // Client ID already exists (primary key violation)
            return false;
        }
    }

    public boolean updateClient(Client client) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE Client SET client_name = ?, status = ? WHERE client_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, client.getClientName());
        stmt.setString(2, client.getStatus());
        stmt.setString(3, client.getClientId());

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public boolean deleteClient(String clientId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM Client WHERE client_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, clientId);

        int rows = stmt.executeUpdate();
        return rows > 0;
    }

    public List<Client> getAllClients() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT client_id, client_name, status FROM Client ORDER BY client_id";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<Client> clients = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("client_id");
            String name = rs.getString("client_name");
            String status = rs.getString("status");
            clients.add(new Client(id, name, status));
        }

        return clients;
    }

    public String getNextClientId() throws SQLException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT MAX(CAST(SUBSTRING(client_id, 2) AS INTEGER)) as max_id FROM Client";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        int nextId = 1;
        if (rs.next() && rs.getInt("max_id") > 0) {
            nextId = rs.getInt("max_id") + 1;
        }

        return String.format("C%04d", nextId);
    }
}