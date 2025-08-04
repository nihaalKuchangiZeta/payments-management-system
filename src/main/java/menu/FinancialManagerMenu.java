package menu;

import java.sql.SQLException;
import java.util.Scanner;
import menu.submenu.*;
import utils.AuditTrailUtils;

public class FinancialManagerMenu {
    private final Scanner scanner;

    public FinancialManagerMenu() {
        this.scanner = new Scanner(System.in);
    }

    public void show() throws SQLException {
        while (true) {
            System.out.println("\n💼 === Financial Manager Menu ===");
            System.out.println("1. Payment Management");
            System.out.println("2. Employee Management");
            System.out.println("3. Vendor Management");
            System.out.println("4. Client Management");
            System.out.println("5. Reports & Insights");
            System.out.println("6. Log Out");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": new PaymentMenu().show(); break;
                case "2": new EmployeeMenu().show(); break;
                case "3": new VendorMenu().show(); break;
                case "4": new ClientMenu().show(); break;
                case "5": new ReportsMenu().show(); break;
                case "6":
                    AuditTrailUtils.logAction("User log out");
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}
