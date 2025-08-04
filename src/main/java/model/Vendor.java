package model;

public class Vendor {
    private String vendorId;
    private String vendorName;
    private String status;

    // Constructor
    public Vendor(String vendorId, String vendorName, String status) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.status = status;
    }

    // Getters
    public String getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "vendorId='" + vendorId + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}