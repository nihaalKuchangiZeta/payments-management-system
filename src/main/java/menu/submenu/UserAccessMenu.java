package menu.submenu;

import service.UserService;

import java.util.Scanner;

public class UserAccessMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService();

    public void show() {
        while (true) {
            System.out.println("\n--- User Role & Access Management ---");
            System.out.println("1. Create New User");
            System.out.println("2. Delete User");
            System.out.println("3. View Users and Roles");
            System.out.println("4. Change Password");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    userService.createUser();
                    break;
                case "2":
                    userService.deleteUser();
                    break;
                case "3":
                    userService.viewUsers();
                    break;
                case "4":
                    userService.changePassword();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
}
