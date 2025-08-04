package menu.submenu;

import service.EmployeeService;

import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final EmployeeService employeeService = new EmployeeService();

    public void show() throws SQLException {
        while (true) {
            System.out.println("\n💼 === Employee Management ===");
            System.out.println("1. Add Employee");
            System.out.println("2. Update Employee");
            System.out.println("3. Remove Employee");
            System.out.println("4. View All Employees");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    employeeService.addEmployee();
                    break;
                case "2":
                    employeeService.updateEmployee();
                    break;
                case "3":
                    employeeService.removeEmployee();
                    break;
                case "4":
                    employeeService.viewAllEmployees();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }

    public void showViewOnly() throws SQLException {
        while (true) {
            System.out.println("\n📋 === View Employees (Read-Only) ===");
            System.out.println("1. View All Employees");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    employeeService.viewAllEmployees();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}
