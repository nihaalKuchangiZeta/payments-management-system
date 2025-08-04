package service;

import dao.PaymentDao;
import model.Payment;
import org.json.JSONObject;
import utils.AuditTrailUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import utils.DateValidator;

public class PaymentService {
    private final PaymentDao paymentDao = new PaymentDao();
    private final Scanner scanner = new Scanner(System.in);

    public void addPayment() throws SQLException {
        System.out.println("== Add New Payment ==");

        System.out.print("Enter Payment Type (Incoming/Outgoing): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter Category (Salary/Vendor Payment/Client Invoice): ");
        String category = scanner.nextLine().trim();

        System.out.print("Enter Counter Party ID: ");
        String counterPartyId = scanner.nextLine().trim();

        System.out.print("Enter Amount: ");
        long amount = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Enter Payment Status (Pending/Processing/Completed): ");
        String status = scanner.nextLine().trim();

        Payment payment = new Payment(type, category, counterPartyId, amount, status);
        boolean success = paymentDao.insertPayment(payment);

        if (success) {
            JSONObject data = new JSONObject();
            data.put("PID", payment.getPid());
            data.put("Type", payment.getType());
            data.put("Category", payment.getCategory());
            data.put("CounterPartyID", payment.getCounterPartyId());
            data.put("Amount", payment.getAmount());
            data.put("Status", payment.getPaymentStatus());
            AuditTrailUtils.logAction("Added new payment", data);
            System.out.println("✅ Payment successfully added.");
        } else {
            System.out.println("❌ Failed to add payment.");
        }
    }

    public void updatePayment() throws SQLException {
        System.out.print("Enter Payment ID to update: ");
        String pid = scanner.nextLine().trim();

        Payment existing = paymentDao.getPaymentById(pid);
        if (existing == null) {
            System.out.println("❌ No payment found with PID " + pid);
            return;
        }

        System.out.println("ℹ️ Press Enter without typing anything to retain the existing value.");

        System.out.print("Enter new Payment Type (" + existing.getType() + "): ");
        String type = scanner.nextLine().trim();
        existing.setType(type.isEmpty() ? existing.getType() : type);

        System.out.print("Enter new Category (" + existing.getCategory() + "): ");
        String category = scanner.nextLine().trim();
        existing.setCategory(category.isEmpty() ? existing.getCategory() : category);

        System.out.print("Enter new Counter Party ID (" + existing.getCounterPartyId() + "): ");
        String cpId = scanner.nextLine().trim();
        existing.setCounterPartyId(cpId.isEmpty() ? existing.getCounterPartyId() : cpId);

        System.out.print("Enter new Amount (" + existing.getAmount() + "): ");
        String amountStr = scanner.nextLine().trim();
        if (!amountStr.isEmpty()) {
            try {
                existing.setAmount(Long.parseLong(amountStr));
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid amount. Keeping the original value.");
            }
        }

        System.out.print("Enter new Status (" + existing.getPaymentStatus() + "): ");
        String status = scanner.nextLine().trim();
        existing.setPaymentStatus(status.isEmpty() ? existing.getPaymentStatus() : status);

        boolean success = paymentDao.updatePayment(existing);
        if (success) {
            JSONObject data = new JSONObject();
            data.put("PID", existing.getPid());
            data.put("Type", existing.getType());
            data.put("Category", existing.getCategory());
            data.put("CounterPartyID", existing.getCounterPartyId());
            data.put("Amount", existing.getAmount());
            data.put("Status", existing.getPaymentStatus());

            AuditTrailUtils.logAction("Updated payment", data);
            System.out.println("✅ Payment updated successfully.");
        } else {
            System.out.println("❌ Failed to update payment.");
        }
    }


    public void deletePayment() throws SQLException {
        System.out.print("Enter Payment ID to delete: ");
        String pid = scanner.nextLine().trim();

        boolean success = paymentDao.deletePayment(pid);
        if (success) {
            JSONObject data = new JSONObject();
            data.put("PID", pid);
            AuditTrailUtils.logAction("Deleted payment", data);
            System.out.println("✅ Payment deleted successfully.");
        } else {
            System.out.println("❌ No payment found with PID " + pid);
        }
    }

    public void viewAllPayments() throws SQLException {
        List<Payment> payments = paymentDao.getAllPayments();
        System.out.println("\n=== All Payments ===");
        for (Payment p : payments) {
            System.out.println("PID: " + p.getPid() + ", Type: " + p.getType() + ", Category: " + p.getCategory() +
                    ", CounterPartyID: " + p.getCounterPartyId() + ", Amount: " + p.getAmount() +
                    ", Status: " + p.getPaymentStatus());
        }
    }

    public void searchPayments() throws SQLException {
        System.out.print("Search by (1) ID, (2) Category, (3) Status, (4) Date Range: ");
        String option = scanner.nextLine().trim();

        switch (option) {
            case "1":
                System.out.print("Enter Payment ID: ");
                String pid = scanner.nextLine().trim();
                Payment p = paymentDao.getPaymentById(pid);
                if (p != null) {
                    System.out.println("PID: " + p.getPid() + ", Type: " + p.getType() + ", Category: " + p.getCategory() +
                            ", CounterPartyID: " + p.getCounterPartyId() + ", Amount: " + p.getAmount() +
                            ", Status: " + p.getPaymentStatus());
                } else {
                    System.out.println("❌ No record found.");
                }
                break;

            case "2":
                System.out.print("Enter Category: ");
                String cat = scanner.nextLine().trim();
                List<Payment> byCat = paymentDao.getPaymentsByCategory(cat);
                if (byCat.isEmpty()) {
                    System.out.println("❌ No payments found for category: " + cat);
                } else {
                    System.out.println("\n=== Payments by Category: " + cat + " ===");
                    for (Payment payment : byCat) {
                        System.out.println("PID: " + payment.getPid() + ", Type: " + payment.getType() + ", Category: " + payment.getCategory() +
                                ", CounterPartyID: " + payment.getCounterPartyId() + ", Amount: " + payment.getAmount() +
                                ", Status: " + payment.getPaymentStatus());
                    }
                }
                break;

            case "3":
                System.out.print("Enter Status: ");
                String status = scanner.nextLine().trim();
                List<Payment> byStatus = paymentDao.getPaymentsByStatus(status);
                if (byStatus.isEmpty()) {
                    System.out.println("❌ No payments found with status: " + status);
                } else {
                    System.out.println("\n=== Payments by Status: " + status + " ===");
                    for (Payment payment : byStatus) {
                        System.out.println("PID: " + payment.getPid() + ", Type: " + payment.getType() + ", Category: " + payment.getCategory() +
                                ", CounterPartyID: " + payment.getCounterPartyId() + ", Amount: " + payment.getAmount() +
                                ", Status: " + payment.getPaymentStatus());
                    }
                }
                break;

            case "4":
                System.out.print("Enter Start Date (YYYY-MM-DD): ");
                String startDate = scanner.nextLine().trim();

                if (!DateValidator.isValidDateFormat(startDate)) {
                    System.out.println("❌ Invalid start date format. Please use YYYY-MM-DD format.");
                    break;
                }

                System.out.print("Enter End Date (YYYY-MM-DD): ");
                String endDate = scanner.nextLine().trim();

                if (!DateValidator.isValidDateFormat(endDate)) {
                    System.out.println("❌ Invalid end date format. Please use YYYY-MM-DD format.");
                    break;
                }

                List<Payment> byDateRange = paymentDao.getPaymentsByDateRange(startDate, endDate);
                if (byDateRange.isEmpty()) {
                    System.out.println("❌ No payments found between " + startDate + " and " + endDate);
                } else {
                    System.out.println("\n=== Payments from " + startDate + " to " + endDate + " ===");
                    for (Payment payment : byDateRange) {
                        System.out.println("PID: " + payment.getPid() + ", Type: " + payment.getType() + ", Category: " + payment.getCategory() +
                                ", CounterPartyID: " + payment.getCounterPartyId() + ", Amount: " + payment.getAmount() +
                                ", Status: " + payment.getPaymentStatus());
                    }
                }
                break;

            default:
                System.out.println("❌ Invalid option.");
        }
    }
}
