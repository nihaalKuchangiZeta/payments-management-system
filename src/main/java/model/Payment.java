package model;

public class Payment {
    private String pid; // Optional at creation
    private String type;
    private String category;
    private String counterPartyId;
    private long amount;
    private String paymentStatus;

    public Payment(String type, String category, String counterPartyId, long amount, String paymentStatus) {
        validateTypeAndCategory(type, category);
        validateStatus(paymentStatus);

        this.type = type;
        this.category = category;
        this.counterPartyId = counterPartyId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Validation logic
    private void validateTypeAndCategory(String type, String category) {
        if (!type.equals("Incoming") && !type.equals("Outgoing")) {
            throw new IllegalArgumentException("Type must be 'Incoming' or 'Outgoing'");
        }

        if (!category.equals("Salary") && !category.equals("Vendor Payment") && !category.equals("Client Invoice")) {
            throw new IllegalArgumentException("Category must be one of 'Salary', 'Vendor Payment', or 'Client Invoice'");
        }

        if (type.equals("Incoming") && !category.equals("Client Invoice")) {
            throw new IllegalArgumentException("Incoming payments must have category 'Client Invoice'");
        }

        if (type.equals("Outgoing") && category.equals("Client Invoice")) {
            throw new IllegalArgumentException("Outgoing payments cannot have category 'Client Invoice'");
        }
    }

    private void validateStatus(String status) {
        if (!status.equals("Pending") && !status.equals("Processing") && !status.equals("Completed")) {
            throw new IllegalArgumentException("Status must be one of 'Pending', 'Processing', or 'Completed'");
        }
    }

    // Getters and Setters
    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }

    public String getType() { return type; }
    public void setType(String type) {
        validateTypeAndCategory(type, this.category); // re-validate with new type
        this.type = type;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        validateTypeAndCategory(this.type, category); // re-validate with new category
        this.category = category;
    }

    public String getCounterPartyId() { return counterPartyId; }
    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public long getAmount() { return amount; }
    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String status) {
        validateStatus(status);
        this.paymentStatus = status;
    }
}
