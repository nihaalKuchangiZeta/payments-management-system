package service;

import dao.ReportsDao;
import utils.DateValidator;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class ReportsService {
    private final Scanner scanner = new Scanner(System.in);
    private final ReportsDao reportsDao = new ReportsDao();
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0");

    public void generateMonthlySummary() throws SQLException {
        System.out.print("Enter month and year (MM-YY) or press Enter for current month: ");
        String monthYear = scanner.nextLine().trim();

        if (monthYear.isEmpty()) {
            // Get current month-year
            LocalDate now = LocalDate.now();
            monthYear = now.format(DateTimeFormatter.ofPattern("MM-yy"));
        }

        // Validate format
        if (!isValidMonthYearFormat(monthYear)) {
            System.out.println("❌ Invalid format. Please use MM-YY format (e.g., 08-25)");
            return;
        }

        System.out.println("\n📊 === Monthly Summary for " + monthYear + " ===");

        // Get summary data
        Map<String, Long> summary = reportsDao.getMonthlySummary(monthYear);
        Map<String, Integer> transactionCount = reportsDao.getMonthlyTransactionCount(monthYear);

        Long totalIncoming = summary.get("Incoming");
        Long totalOutgoing = summary.get("Outgoing");
        Integer incomingCount = transactionCount.get("Incoming");
        Integer outgoingCount = transactionCount.get("Outgoing");

        // Display results
        System.out.println("💰 Total Incoming: Rs." + currencyFormat.format(totalIncoming) +
                " (" + incomingCount + " transactions)");
        System.out.println("💸 Total Outgoing: Rs." + currencyFormat.format(totalOutgoing) +
                " (" + outgoingCount + " transactions)");

        Long netAmount = totalIncoming - totalOutgoing;
        String netStatus = netAmount >= 0 ? "✅ Net Positive" : "❌ Net Negative";
        System.out.println("📈 Net Amount: Rs." + currencyFormat.format(netAmount) + " (" + netStatus + ")");
        System.out.println("📊 Total Transactions: " + (incomingCount + outgoingCount));
    }

    public void generateCategoryBreakdown() throws SQLException {
        System.out.println("\n📋 === Category-wise Breakdown ===");

        Map<String, Map<String, Object>> breakdown = reportsDao.getCategoryBreakdown();

        if (breakdown.isEmpty()) {
            System.out.println("❌ No payment data available.");
            return;
        }

        for (Map.Entry<String, Map<String, Object>> entry : breakdown.entrySet()) {
            String category = entry.getKey();
            Map<String, Object> data = entry.getValue();

            System.out.println("\n🏷️  " + category.toUpperCase());

            // Incoming data
            Integer incomingCount = (Integer) data.getOrDefault("Incoming_count", 0);
            Long incomingTotal = (Long) data.getOrDefault("Incoming_total", 0L);
            if (incomingCount > 0) {
                System.out.println("   💰 Incoming: Rs." + currencyFormat.format(incomingTotal) +
                        " (" + incomingCount + " transactions)");
            }

            // Outgoing data
            Integer outgoingCount = (Integer) data.getOrDefault("Outgoing_count", 0);
            Long outgoingTotal = (Long) data.getOrDefault("Outgoing_total", 0L);
            if (outgoingCount > 0) {
                System.out.println("   💸 Outgoing: Rs." + currencyFormat.format(outgoingTotal) +
                        " (" + outgoingCount + " transactions)");
            }

            // Category net
            Long categoryNet = incomingTotal - outgoingTotal;
            System.out.println("   📊 Net: Rs." + currencyFormat.format(categoryNet));
        }

        // Show top categories
        System.out.println("\n🔝 === Top Categories by Amount ===");
        List<Map<String, Object>> topCategories = reportsDao.getTopCategoriesByAmount(5);

        int rank = 1;
        for (Map<String, Object> category : topCategories) {
            System.out.println(rank + ". " + category.get("category") +
                    " - Rs." + currencyFormat.format((Long) category.get("total")) +
                    " (" + category.get("count") + " transactions)");
            rank++;
        }
    }

    public void generatePaymentStatusSummary() throws SQLException {
        System.out.println("\n📈 === Payment Status Summary ===");

        Map<String, Map<String, Object>> statusSummary = reportsDao.getPaymentStatusSummary();

        if (statusSummary.isEmpty()) {
            System.out.println("❌ No payment data available.");
            return;
        }

        Long totalAmount = 0L;
        Integer totalCount = 0;

        for (Map.Entry<String, Map<String, Object>> entry : statusSummary.entrySet()) {
            String status = entry.getKey();
            Map<String, Object> data = entry.getValue();

            Integer count = (Integer) data.get("count");
            Long total = (Long) data.get("total");

            totalAmount += total;
            totalCount += count;

            // Status emoji
            String statusEmoji = getStatusEmoji(status);

            System.out.println(statusEmoji + " " + status.toUpperCase() + ": Rs." +
                    currencyFormat.format(total) + " (" + count + " payments)");
        }

        System.out.println("\n📊 OVERALL TOTALS:");
        System.out.println("💰 Total Amount: Rs." + currencyFormat.format(totalAmount));
        System.out.println("📄 Total Payments: " + totalCount);

        // Calculate percentages
        System.out.println("\n📈 STATUS DISTRIBUTION:");
        for (Map.Entry<String, Map<String, Object>> entry : statusSummary.entrySet()) {
            String status = entry.getKey();
            Map<String, Object> data = entry.getValue();

            Integer count = (Integer) data.get("count");
            Long total = (Long) data.get("total");

            double countPercentage = (double) count / totalCount * 100;
            double amountPercentage = (double) total / totalAmount * 100;

            System.out.println("• " + status + ": " +
                    String.format("%.1f%% of transactions, %.1f%% of amount",
                            countPercentage, amountPercentage));
        }
    }

    private boolean isValidMonthYearFormat(String monthYear) {
        if (monthYear.length() != 5 || monthYear.charAt(2) != '-') {
            return false;
        }

        try {
            String[] parts = monthYear.split("-");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            return month >= 1 && month <= 12 && year >= 0 && year <= 99;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getStatusEmoji(String status) {
        switch (status.toLowerCase()) {
            case "pending": return "⏳";
            case "processing": return "🔄";
            case "completed": return "✅";
            default: return "📋";
        }
    }

    // Additional utility method for custom date range reports
    public void generateCustomDateRangeReport() throws SQLException {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine().trim();

        if (!DateValidator.isValidDateFormat(startDate)) {
            System.out.println("❌ Invalid start date format. Please use YYYY-MM-DD format.");
            return;
        }

        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine().trim();

        if (!DateValidator.isValidDateFormat(endDate)) {
            System.out.println("❌ Invalid end date format. Please use YYYY-MM-DD format.");
            return;
        }

        if (!DateValidator.isValidDateRange(startDate, endDate)) {
            System.out.println("❌ Invalid date range. Start date must be before or equal to end date.");
            return;
        }

        System.out.println("\n📅 === Custom Date Range Report (" + startDate + " to " + endDate + ") ===");

        var payments = reportsDao.getPaymentsForDateRange(startDate, endDate);

        if (payments.isEmpty()) {
            System.out.println("❌ No payments found in the specified date range.");
            return;
        }

        // Calculate summary
        Long totalIncoming = 0L, totalOutgoing = 0L;
        Integer incomingCount = 0, outgoingCount = 0;

        for (var payment : payments) {
            if ("Incoming".equals(payment.getType())) {
                totalIncoming += payment.getAmount();
                incomingCount++;
            } else {
                totalOutgoing += payment.getAmount();
                outgoingCount++;
            }
        }

        System.out.println("💰 Total Incoming: Rs." + currencyFormat.format(totalIncoming) +
                " (" + incomingCount + " transactions)");
        System.out.println("💸 Total Outgoing: Rs." + currencyFormat.format(totalOutgoing) +
                " (" + outgoingCount + " transactions)");
        System.out.println("📈 Net Amount: Rs." + currencyFormat.format(totalIncoming - totalOutgoing));
        System.out.println("📊 Total Payments: " + payments.size());
    }
}