package menu;

import model.User;
import java.sql.SQLException;

public class MenuRouter {

    public static void showMenu(User user) {
        String role = user.getRole().toLowerCase();
        try {
            switch (role) {
                case "admin":
                    new AdminMenu().show();
                    break;
                case "viewer":
                    new ViewerMenu().show();
                    break;
                case "financialmanager":
                    new FinancialManagerMenu().show();
                    break;
                default:
                    System.out.println("❌ Unknown role: " + role);
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Database error occurred.");
            e.printStackTrace();
        }
    }
}
