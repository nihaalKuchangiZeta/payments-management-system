package menu.submenu;

import java.util.Scanner;

public class ReportsMenu {
    private final Scanner scanner = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("\n--- Reports & Insights ---");
            System.out.println("1. Monthly Summary");
            System.out.println("2. Category-wise Breakdown");
            System.out.println("3. Payment Status Summary");
            System.out.println("4. Export to CSV/PDF");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "0": return;
                default: System.out.println("Functionality coming soon!");
            }
        }
    }
}
