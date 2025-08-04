package menu.submenu;

import service.AuditTrailService;
import java.util.Scanner;

public class AuditMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AuditTrailService auditService = new AuditTrailService();

    public void show() {
        while (true) {
            System.out.println("\n--- Audit Trail & Logs ---");
            System.out.println("1. View All Logs");
            System.out.println("2. Filter Logs by Date");
            System.out.println("3. Filter Logs by Action");
            System.out.println("4. Filter Logs by User");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    auditService.viewAllLogs();
                    break;
                case "2":
                    auditService.filterLogsByDate();
                    break;
                case "3":
                    auditService.filterLogsByAction();
                    break;
                case "4":
                    auditService.filterLogsByUser();
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