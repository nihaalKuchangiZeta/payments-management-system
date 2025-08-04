package menu.submenu;

import service.PaymentService;

import java.sql.SQLException;
import java.util.Scanner;

public class PaymentMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentService paymentService = new PaymentService();

    public void show() {
        while (true) {
            System.out.println("\n💳 === Payment Management ===");
            System.out.println("1. Add New Payment");
            System.out.println("2. Update Payment");
            System.out.println("3. Delete Payment");
            System.out.println("4. View All Payments");
            System.out.println("5. Search Payments by ID / Category / Date / Status");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        paymentService.addPayment();
                        break;
                    case "2":
                        paymentService.updatePayment();
                        break;
                    case "3":
                        paymentService.deletePayment();
                        break;
                    case "4":
                        paymentService.viewAllPayments();
                        break;
                    case "5":
                        paymentService.searchPayments();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("❌ Invalid choice.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Database error occurred: " + e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("❌ Invalid number format: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Invalid input: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void showViewOnly() {
        while (true) {
            System.out.println("\n📄 === View Payments (Read-Only) ===");
            System.out.println("1. View All Payments");
            System.out.println("2. Search Payments by ID / Category / Date / Status");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        paymentService.viewAllPayments();
                        break;
                    case "2":
                        paymentService.searchPayments();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("❌ Invalid choice.");
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
