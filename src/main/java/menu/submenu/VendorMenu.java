package menu.submenu;

import service.VendorService;
import java.util.Scanner;

public class VendorMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final VendorService vendorService = new VendorService();

    public void show() {
        while (true) {
            System.out.println("\n--- Vendor Management ---");
            System.out.println("1. Register Vendor");
            System.out.println("2. Update Vendor");
            System.out.println("3. Remove Vendor");
            System.out.println("4. View Vendors");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    vendorService.registerVendor();
                    break;
                case "2":
                    vendorService.updateVendor();
                    break;
                case "3":
                    vendorService.removeVendor();
                    break;
                case "4":
                    vendorService.viewVendors();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❌ Invalid option. Please try again.");
                    break;
            }
        }
    }
}