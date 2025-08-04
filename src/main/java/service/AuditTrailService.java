package service;

import dao.AuditTrailDao;
import model.AuditTrail;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class AuditTrailService {
    private AuditTrailDao auditTrailDao = new AuditTrailDao();
    private Scanner input = new Scanner(System.in);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void viewAllLogs() {
        System.out.println("== View All Audit Logs ==");
        try {
            List<AuditTrail> logs = auditTrailDao.getAllLogs();
            if (logs.isEmpty()) {
                System.out.println("⚠️ No audit logs found.");
                return;
            }

            displayLogs(logs);
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving audit logs: " + e.getMessage());
        }
    }

    public void filterLogsByDate() {
        System.out.println("== Filter Logs by Date ==");
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = input.nextLine().trim();

        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = input.nextLine().trim();

        // Basic date format validation
        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }

        try {
            List<AuditTrail> logs = auditTrailDao.getLogsByDateRange(startDate, endDate);
            if (logs.isEmpty()) {
                System.out.println("⚠️ No audit logs found for the specified date range.");
                return;
            }

            System.out.println("📅 Logs from " + startDate + " to " + endDate + ":");
            displayLogs(logs);
        } catch (SQLException e) {
            System.out.println("❌ Error filtering logs by date: " + e.getMessage());
        }
    }

    public void filterLogsByAction() {
        System.out.println("== Filter Logs by Action ==");

        try {
            List<String> actions = auditTrailDao.getDistinctActions();
            if (actions.isEmpty()) {
                System.out.println("⚠️ No actions found in audit logs.");
                return;
            }

            System.out.println("Available Actions:");
            for (int i = 0; i < actions.size(); i++) {
                System.out.println((i + 1) + ". " + actions.get(i));
            }

            System.out.print("Select action number: ");
            String inputStr = input.nextLine().trim();
            int index;

            try {
                index = Integer.parseInt(inputStr) - 1;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                return;
            }

            if (index < 0 || index >= actions.size()) {
                System.out.println("❌ Invalid action number.");
                return;
            }

            String selectedAction = actions.get(index);
            List<AuditTrail> logs = auditTrailDao.getLogsByAction(selectedAction);

            if (logs.isEmpty()) {
                System.out.println("⚠️ No audit logs found for action: " + selectedAction);
                return;
            }

            System.out.println("🔍 Logs for action: " + selectedAction);
            displayLogs(logs);
        } catch (SQLException e) {
            System.out.println("❌ Error filtering logs by action: " + e.getMessage());
        }
    }


    public void filterLogsByUser() {
        System.out.println("== Filter Logs by User ==");

        try {
            List<String> users = auditTrailDao.getDistinctUsers();
            if (users.isEmpty()) {
                System.out.println("⚠️ No users found in audit logs.");
                return;
            }

            System.out.println("Available Users:");
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i));
            }

            System.out.print("Select user number (or type username): ");
            String userInput = input.nextLine().trim();
            String selectedUser;

            try {
                int userIndex = Integer.parseInt(userInput) - 1;
                if (userIndex >= 0 && userIndex < users.size()) {
                    selectedUser = users.get(userIndex);
                } else {
                    System.out.println("❌ Invalid user number.");
                    return;
                }
            } catch (NumberFormatException e) {
                selectedUser = userInput;
            }

            List<AuditTrail> logs = auditTrailDao.getLogsByUser(selectedUser);
            if (logs.isEmpty()) {
                System.out.println("⚠️ No audit logs found for user: " + selectedUser);
                return;
            }

            System.out.println("👤 Logs for user: " + selectedUser);
            displayLogs(logs);
        } catch (SQLException e) {
            System.out.println("❌ Error filtering logs by user: " + e.getMessage());
        }
    }

    private void displayLogs(List<AuditTrail> logs) {
        System.out.println("\n==================== AUDIT LOGS ====================");
        for (AuditTrail log : logs) {
            System.out.println("ID        : " + log.getId());
            System.out.println("Timestamp : " + dateFormat.format(log.getTimestamp()));
            System.out.println("User      : " + log.getUser());
            System.out.println("Role      : " + log.getRole());
            System.out.println("Action    : " + log.getAction());
            System.out.println("Details   : " + log.getAdditionalDetails());
            System.out.println("---------------------------------------------------");
        }
        System.out.println("Total logs: " + logs.size());
    }

    private boolean isValidDateFormat(String date) {
        if (date.length() != 10) return false;
        String[] parts = date.split("-");
        if (parts.length != 3) return false;

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            return year > 1900 && year < 3000 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}