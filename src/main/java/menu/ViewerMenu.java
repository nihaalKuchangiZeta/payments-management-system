package menu;

import java.sql.SQLException;
import java.util.Scanner;
import menu.submenu.*;

public class ViewerMenu {
    private final Scanner scanner;

    public ViewerMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void show() throws SQLException {
        while (true) {
            System.out.println("\n👁️ === Viewer Menu ===");
            System.out.println("1. View Payments");
            System.out.println("2. View Employees");
            System.out.println("3. View Vendors");
            System.out.println("4. View Clients");
            System.out.println("5. Reports & Insights");
            System.out.println("0. Log Out");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    new PaymentMenu().showViewOnly(); // viewer-specific payment view
                    break;
                case "2":
                    new EmployeeMenu().showViewOnly(); // viewer-specific employee view
                    break;
                case "3":
                    new VendorMenu().showViewOnly(); // viewer-specific vendor view
                    break;
                case "4":
                    new ClientMenu().showViewOnly(); // viewer-specific client view
                    break;
                case "5":
                    new ReportsMenu().show(); // assuming same ReportsMenu is shared
                    break;
                case "0":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}
