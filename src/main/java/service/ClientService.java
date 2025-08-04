package service;

import dao.ClientDao;
import model.Client;
import utils.AuditTrailUtils;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ClientService {
    private ClientDao clientDao = new ClientDao();
    private Scanner input = new Scanner(System.in);

    public void registerClient() {
        System.out.println("== Register New Client ==");

        try {
            String clientId = clientDao.getNextClientId();
            System.out.println("Generated Client ID: " + clientId);

            System.out.print("Enter Client Name: ");
            String clientName = input.nextLine();

            Client newClient = new Client(clientId, clientName, "Active");

            if (clientDao.insertClient(newClient)) {
                System.out.println("✅ Client registered successfully with ID: " + clientId);

                JSONObject data = new JSONObject();
                data.put("CLIENT_ID", newClient.getClientId());
                data.put("CLIENT_NAME", newClient.getClientName());
                data.put("STATUS", newClient.getStatus());
                AuditTrailUtils.logAction("Registered new client", data);
            } else {
                System.out.println("❌ Failed to register client.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error registering client: " + e.getMessage());
        }
    }

    public void updateClient() {
        System.out.println("== Update Client ==");
        System.out.print("Enter Client ID to update: ");
        String clientId = input.next();

        try {
            Client existingClient = clientDao.getClientById(clientId);
            if (existingClient == null) {
                System.out.println("❌ Client not found with ID: " + clientId);
                return;
            }

            System.out.println("Current Details:");
            System.out.println("Name: " + existingClient.getClientName());
            System.out.println("Status: " + existingClient.getStatus());

            System.out.print("Enter new Client Name (or press Enter to keep current): ");
            input.nextLine(); // Clear buffer
            String newName = input.nextLine();
            if (newName.trim().isEmpty()) {
                newName = existingClient.getClientName();
            }

            System.out.print("Enter new Status (Active/Inactive) (or press Enter to keep current): ");
            String newStatus = input.nextLine();
            if (newStatus.trim().isEmpty()) {
                newStatus = existingClient.getStatus();
            } else if (!newStatus.equalsIgnoreCase("Active") && !newStatus.equalsIgnoreCase("Inactive")) {
                System.out.println("❌ Invalid status. Please enter 'Active' or 'Inactive'.");
                return;
            }

            Client updatedClient = new Client(clientId, newName, newStatus);

            if (clientDao.updateClient(updatedClient)) {
                System.out.println("✅ Client updated successfully.");

                JSONObject data = new JSONObject();
                data.put("CLIENT_ID", updatedClient.getClientId());
                data.put("OLD_NAME", existingClient.getClientName());
                data.put("NEW_NAME", updatedClient.getClientName());
                data.put("OLD_STATUS", existingClient.getStatus());
                data.put("NEW_STATUS", updatedClient.getStatus());
                AuditTrailUtils.logAction("Updated client", data);
            } else {
                System.out.println("❌ Failed to update client.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating client: " + e.getMessage());
        }
    }

    public void removeClient() {
        System.out.println("== Remove Client ==");
        System.out.print("Enter Client ID to remove: ");
        String clientId = input.next();

        try {
            Client existingClient = clientDao.getClientById(clientId);
            if (existingClient == null) {
                System.out.println("❌ Client not found with ID: " + clientId);
                return;
            }

            System.out.println("Client Details:");
            System.out.println("ID: " + existingClient.getClientId());
            System.out.println("Name: " + existingClient.getClientName());
            System.out.println("Status: " + existingClient.getStatus());

            System.out.print("Are you sure you want to remove this client? (Y/N): ");
            String confirmation = input.next();

            if (confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("Yes")) {
                if (clientDao.deleteClient(clientId)) {
                    System.out.println("✅ Client removed successfully.");

                    JSONObject data = new JSONObject();
                    data.put("CLIENT_ID", existingClient.getClientId());
                    data.put("CLIENT_NAME", existingClient.getClientName());
                    AuditTrailUtils.logAction("Removed client", data);
                } else {
                    System.out.println("❌ Failed to remove client.");
                }
            } else {
                System.out.println("❌ Client removal cancelled.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error removing client: " + e.getMessage());
        }
    }

    public void viewClients() {
        System.out.println("== View Clients ==");
        try {
            List<Client> clients = clientDao.getAllClients();
            if (clients.isEmpty()) {
                System.out.println("⚠️ No clients found.");
                return;
            }

            System.out.printf("%-10s %-30s %-10s\n", "Client ID", "Client Name", "Status");
            System.out.println("-----------------------------------------------------");
            for (Client client : clients) {
                System.out.printf("%-10s %-30s %-10s\n",
                        client.getClientId(),
                        client.getClientName(),
                        client.getStatus());
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving clients: " + e.getMessage());
        }
    }
}