package service;

import dao.VendorDao;
import model.Vendor;
import utils.AuditTrailUtils;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class VendorService {
    private VendorDao vendorDao = new VendorDao();
    private Scanner input = new Scanner(System.in);

    public void registerVendor() {
        System.out.println("== Register New Vendor ==");

        try {
            String vendorId = vendorDao.getNextVendorId();
            System.out.println("Generated Vendor ID: " + vendorId);

            System.out.print("Enter Vendor Name: ");
            String vendorName = input.nextLine();

            Vendor newVendor = new Vendor(vendorId, vendorName, "Active");

            if (vendorDao.insertVendor(newVendor)) {
                System.out.println("✅ Vendor registered successfully with ID: " + vendorId);

                JSONObject data = new JSONObject();
                data.put("VENDOR_ID", newVendor.getVendorId());
                data.put("VENDOR_NAME", newVendor.getVendorName());
                data.put("STATUS", newVendor.getStatus());
                AuditTrailUtils.logAction("Registered new vendor", data);
            } else {
                System.out.println("❌ Failed to register vendor.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error registering vendor: " + e.getMessage());
        }
    }

    public void updateVendor() {
        System.out.println("== Update Vendor ==");
        System.out.print("Enter Vendor ID to update: ");
        String vendorId = input.next();

        try {
            Vendor existingVendor = vendorDao.getVendorById(vendorId);
            if (existingVendor == null) {
                System.out.println("❌ Vendor not found with ID: " + vendorId);
                return;
            }

            System.out.println("Current Details:");
            System.out.println("Name: " + existingVendor.getVendorName());
            System.out.println("Status: " + existingVendor.getStatus());

            System.out.print("Enter new Vendor Name (or press Enter to keep current): ");
            //input.nextLine(); // Clear buffer
            String newName = input.nextLine();
            if (newName.trim().isEmpty()) {
                newName = existingVendor.getVendorName();
            }

            System.out.print("Enter new Status (Active/Inactive) (or press Enter to keep current): ");
            String newStatus = input.nextLine();
            if (newStatus.trim().isEmpty()) {
                newStatus = existingVendor.getStatus();
            } else if (!newStatus.equalsIgnoreCase("Active") && !newStatus.equalsIgnoreCase("Inactive")) {
                System.out.println("❌ Invalid status. Please enter 'Active' or 'Inactive'.");
                return;
            }

            Vendor updatedVendor = new Vendor(vendorId, newName, newStatus);

            if (vendorDao.updateVendor(updatedVendor)) {
                System.out.println("✅ Vendor updated successfully.");

                JSONObject data = new JSONObject();
                data.put("VENDOR_ID", updatedVendor.getVendorId());
                data.put("OLD_NAME", existingVendor.getVendorName());
                data.put("NEW_NAME", updatedVendor.getVendorName());
                data.put("OLD_STATUS", existingVendor.getStatus());
                data.put("NEW_STATUS", updatedVendor.getStatus());
                AuditTrailUtils.logAction("Updated vendor", data);
            } else {
                System.out.println("❌ Failed to update vendor.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating vendor: " + e.getMessage());
        }
    }

    public void removeVendor() {
        System.out.println("== Remove Vendor ==");
        System.out.print("Enter Vendor ID to remove: ");
        String vendorId = input.next();

        try {
            Vendor existingVendor = vendorDao.getVendorById(vendorId);
            if (existingVendor == null) {
                System.out.println("❌ Vendor not found with ID: " + vendorId);
                return;
            }

            System.out.println("Vendor Details:");
            System.out.println("ID: " + existingVendor.getVendorId());
            System.out.println("Name: " + existingVendor.getVendorName());
            System.out.println("Status: " + existingVendor.getStatus());

            System.out.print("Are you sure you want to remove this vendor? (Y/N): ");
            String confirmation = input.next();

            if (confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("Yes")) {
                if (vendorDao.deleteVendor(vendorId)) {
                    System.out.println("✅ Vendor removed successfully.");

                    JSONObject data = new JSONObject();
                    data.put("VENDOR_ID", existingVendor.getVendorId());
                    data.put("VENDOR_NAME", existingVendor.getVendorName());
                    AuditTrailUtils.logAction("Removed vendor", data);
                } else {
                    System.out.println("❌ Failed to remove vendor.");
                }
            } else {
                System.out.println("❌ Vendor removal cancelled.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error removing vendor: " + e.getMessage());
        }
    }

    public void viewVendors() {
        System.out.println("== View Vendors ==");
        try {
            List<Vendor> vendors = vendorDao.getAllVendors();
            if (vendors.isEmpty()) {
                System.out.println("⚠️ No vendors found.");
                return;
            }

            System.out.printf("%-10s %-30s %-10s\n", "Vendor ID", "Vendor Name", "Status");
            System.out.println("-----------------------------------------------------");
            for (Vendor vendor : vendors) {
                System.out.printf("%-10s %-30s %-10s\n",
                        vendor.getVendorId(),
                        vendor.getVendorName(),
                        vendor.getStatus());
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving vendors: " + e.getMessage());
        }
    }
}