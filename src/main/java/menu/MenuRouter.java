package menu;

import model.User;

import java.sql.SQLException;

public class MenuRouter {

    public static void showMenu(User user) {
        String role = user.getRole().toLowerCase();
        try {
            switch (role) {
                case "admin":
                    AdminMenu adminMenu = new AdminMenu();
                    adminMenu.show();
                    break;
                case "viewer":
                    //ViewerMenu.show();
                    break;
                case "financialmanager":
                    FinancialManagerMenu financialManagerMenu = new FinancialManagerMenu();
                    financialManagerMenu.show();
                    break;
                default:
                    System.out.println("Unknown role: " + role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
