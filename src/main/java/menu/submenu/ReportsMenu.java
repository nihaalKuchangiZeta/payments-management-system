package menu.submenu;

import service.ReportsService;

import java.sql.SQLException;
import java.util.Scanner;

public class ReportsMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final ReportsService reportsService = new ReportsService();

    public void show() {
        while (true) {
            System.out.println("\n📊 === Reports & Insights ===");
            System.out.println("1. Monthly Summary");
            System.out.println("2. Category-wise Breakdown");
            System.out.println("3. Payment Status Summary");
            System.out.println("4. Custom Date Range Report");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        reportsService.generateMonthlySummary();
                        break;
                    case "2":
                        reportsService.generateCategoryBreakdown();
                        break;
                    case "3":
                        reportsService.generatePaymentStatusSummary();
                        break;
                    case "4":
                        reportsService.generateCustomDateRangeReport();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("❌ Invalid choice. Please select 1-4 or 0.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Database error occurred: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("❌ An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}