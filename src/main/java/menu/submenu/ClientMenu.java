package menu.submenu;

import service.ClientService;
import java.util.Scanner;

public class ClientMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final ClientService clientService = new ClientService();

    public void show() {
        while (true) {
            System.out.println("\n--- Client Management ---");
            System.out.println("1. Register Client");
            System.out.println("2. Update Client");
            System.out.println("3. Remove Client");
            System.out.println("4. View Clients");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    clientService.registerClient();
                    break;
                case "2":
                    clientService.updateClient();
                    break;
                case "3":
                    clientService.removeClient();
                    break;
                case "4":
                    clientService.viewClients();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
                    break;
            }
        }
    }

    public void showViewOnly() {
        while (true) {
            System.out.println("\n📋 --- View Clients (Read-Only) ---");
            System.out.println("1. View Clients");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    clientService.viewClients();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
                    break;
            }
        }
    }
}
