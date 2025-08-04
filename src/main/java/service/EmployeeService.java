package service;

import model.Employee;
import dao.EmployeeDao;
import org.json.JSONObject;
import utils.AuditTrailUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmployeeService {
    private final EmployeeDao employeeDao;
    private final Scanner scanner = new Scanner(System.in);

    public EmployeeService() {
        this.employeeDao = new EmployeeDao();
    }

    public void addEmployee() throws SQLException {
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();

        System.out.print("Enter department: ");
        String department = scanner.nextLine();

        System.out.print("Enter salary: ");
        long salary = Long.parseLong(scanner.nextLine());

        String eid = generateEmployeeId();
        Employee employee = new Employee(eid, name, department, salary);
        employeeDao.insert(employee);

        JSONObject data = new JSONObject();
        data.put("EID", eid);
        data.put("NAME", name);
        data.put("DEPARTMENT", department);
        data.put("SALARY", salary);
        AuditTrailUtils.logAction("Added new employee", data);

        System.out.println("✅ Employee " + name + " (" + eid +")" +" added successfully.");
    }

    public void updateEmployee() throws SQLException {
        System.out.println("== Update Employee ==");
        System.out.print("Enter Employee ID to update: ");
        String eid = scanner.nextLine();

        try {
            Employee existingEmployee = employeeDao.getEmployeeById(eid);
            if (existingEmployee == null) {
                System.out.println("❌ Employee not found with ID: " + eid);
                return;
            }

            System.out.println("Current Details:");
            System.out.println("Name: " + existingEmployee.getName());
            System.out.println("Department: " + existingEmployee.getDepartment());
            System.out.println("Salary: " + existingEmployee.getSalary());

            System.out.print("Enter new name (or press Enter to keep current): ");
            String newName = scanner.nextLine();
            if (newName.trim().isEmpty()) {
                newName = existingEmployee.getName();
            }

            System.out.print("Enter new department (or press Enter to keep current): ");
            String newDepartment = scanner.nextLine();
            if (newDepartment.trim().isEmpty()) {
                newDepartment = existingEmployee.getDepartment();
            }

            System.out.print("Enter new salary (or press Enter to keep current): ");
            String salaryInput = scanner.nextLine();
            long newSalary;
            if (salaryInput.trim().isEmpty()) {
                newSalary = existingEmployee.getSalary();
            } else {
                try {
                    newSalary = Long.parseLong(salaryInput);
                    if (newSalary < 0) {
                        System.out.println("❌ Invalid salary. Salary must be a positive number.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid salary format. Please enter a valid number.");
                    return;
                }
            }

            Employee updatedEmployee = new Employee(eid, newName, newDepartment, newSalary);

            if (employeeDao.update(updatedEmployee)) {
                System.out.println("✅ Employee updated successfully.");

                JSONObject data = new JSONObject();
                data.put("EID", updatedEmployee.getEid());
                data.put("OLD_NAME", existingEmployee.getName());
                data.put("NEW_NAME", updatedEmployee.getName());
                data.put("OLD_DEPARTMENT", existingEmployee.getDepartment());
                data.put("NEW_DEPARTMENT", updatedEmployee.getDepartment());
                data.put("OLD_SALARY", existingEmployee.getSalary());
                data.put("NEW_SALARY", updatedEmployee.getSalary());
                AuditTrailUtils.logAction("Updated employee", data);
            } else {
                System.out.println("❌ Failed to update employee.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating employee: " + e.getMessage());
        }
    }
    public void removeEmployee() throws SQLException {
        System.out.print("Enter Employee ID to remove: ");
        String eid = scanner.nextLine();

        boolean deleted = employeeDao.delete(eid);

        if (deleted) {
            JSONObject data = new JSONObject();
            data.put("EID", eid);
            AuditTrailUtils.logAction("Removed employee", data);

            System.out.println("✅ Employee (" + eid + ") removed successfully.");
        } else {
            System.out.println("❌ No employee found with ID: " + eid);
        }
    }


    public void viewAllEmployees() throws SQLException {
        List<Employee> employees = employeeDao.getAll();

        if (employees.isEmpty()) {
            System.out.println("⚠ No employees found.");
            return;
        }

        System.out.println("\n📋 Employee List:");
        for (Employee emp : employees) {
            System.out.println("ID: " + emp.getEid());
            System.out.println("Name: " + emp.getName());
            System.out.println("Department: " + emp.getDepartment());
            System.out.println("Salary: " + emp.getSalary());
            System.out.println("------------------------");
        }
    }

    private String generateEmployeeId() throws SQLException {
        return employeeDao.getNextEmployeeId();
    }
}

