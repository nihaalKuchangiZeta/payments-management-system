package menu;

import java.sql.SQLException;
import java.util.Scanner;

import menu.submenu.*;

public class AdminMenu {
    private final Scanner scanner;

    public AdminMenu() {
        scanner = new Scanner(System.in);
    }

    public void show() throws SQLException {
        while (true) {
            System.out.println("\n\u2705 === Admin Menu ===");
            System.out.println("1. Payment Management");
            System.out.println("2. Employee Management");
            System.out.println("3. Vendor Management");
            System.out.println("4. Client Management");
            System.out.println("5. User Role & Access Management");
            System.out.println("6. Audit Trail & Logs");
            System.out.println("7. Reports & Insights");
            System.out.println("0. Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": new PaymentMenu().show(); break;
                case "2": new EmployeeMenu().show(); break;
                case "3": new VendorMenu().show(); break;
                case "4": new ClientMenu().show(); break;
                case "5": new UserAccessMenu().show(); break;
                case "6": new AuditMenu().show(); break;
                case "7": new ReportsMenu().show(); break;
                case "0": System.out.println("Logging out..."); return;
                default: System.out.println("\u274C Invalid choice.");
            }
        }
    }
}
