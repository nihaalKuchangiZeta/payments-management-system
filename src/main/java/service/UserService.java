package service;

import dao.UserDao;
import model.User;
import utils.PasswordUtils;
import utils.AuditTrailUtils;
import org.json.JSONObject;
import utils.SessionContext;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private UserDao userDao = new UserDao();
    private Scanner input = new Scanner(System.in);

    public User userLoginIn() {
        System.out.print("Enter your username: ");
        String username = input.next();

        System.out.print("Enter your password: ");
        String password = input.next();

        try {
            if (checkValidUser(username, password)) {
                User user = userDao.getUserByUsername(username);
                SessionContext.setCurrentUser(user);
                System.out.println("✅ Successful login of " + user.getRole() + " " + user.getUsername() + " in KEL:PMS.");

                AuditTrailUtils.logAction("User login");

                return user;
            } else {
                System.out.println("❌ Invalid username or password.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error during login: " + e.getMessage());
            return null;
        }
    }

    public boolean checkValidUser(String username, String plainPassword) throws SQLException {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        return PasswordUtils.checkPassword(plainPassword, user.getHashedPassword());
    }

    public void createUser() {
        System.out.println("== Create New User ==");
        System.out.print("Enter Username: ");
        String username = input.next();

        System.out.print("Enter Password: ");
        String password = input.next();

        System.out.print("Enter Role (Admin/FinancialManager/Viewer): ");
        String role = input.next();

        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User(username, hashedPassword, role);

        try {
            if (userDao.insertUser(newUser)) {
                System.out.println("✅ User created successfully.");

                JSONObject data = new JSONObject();
                data.put("CREATED_USERNAME", newUser.getUsername());
                data.put("CREATED_ROLE", newUser.getRole());
                AuditTrailUtils.logAction("Created new user", data);
            } else {
                System.out.println("❌ Failed to create user. Username may already exist.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error creating user: " + e.getMessage());
        }
    }

    public void changePassword() {
        System.out.println("== Change Password ==");
        System.out.print("Enter Username: ");
        String username = input.next();

        System.out.print("Enter Old Password: ");
        String oldPassword = input.next();

        try {
            if (!checkValidUser(username, oldPassword)) {
                System.out.println("❌ Invalid credentials. Cannot change password.");
                return;
            }

            System.out.print("Enter New Password: ");
            String newPassword = input.next();

            String hashedPassword = PasswordUtils.hashPassword(newPassword);
            if (userDao.updatePassword(username, hashedPassword)) {
                System.out.println("✅ Password updated successfully.");

                JSONObject data = new JSONObject();
                data.put("CHANGED_USERNAME", username);
                AuditTrailUtils.logAction("Changed password", data);
            } else {
                System.out.println("❌ Failed to update password.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error changing password: " + e.getMessage());
        }
    }

    public void deleteUser() {
        System.out.println("== Delete User ==");
        System.out.print("Enter Username to delete: ");
        String username = input.next();

        try {
            if (userDao.deleteUser(username)) {
                System.out.println("✅ User deleted successfully.");

                JSONObject data = new JSONObject();
                data.put("DELETED_USERNAME", username);
                AuditTrailUtils.logAction("Deleted user", data);
            } else {
                System.out.println("❌ User not found or could not be deleted.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error deleting user: " + e.getMessage());
        }
    }

    public void viewUsers() {
        System.out.println("== View Users ==");
        try {
            List<User> users = userDao.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("⚠️ No users found.");
                return;
            }
            System.out.printf("%-20s %-15s\n", "Username", "Role");
            System.out.println("-------------------------------");
            for (User user : users) {
                System.out.printf("%-20s %-15s\n", user.getUsername(), user.getRole());
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving users: " + e.getMessage());
        }
    }
}
