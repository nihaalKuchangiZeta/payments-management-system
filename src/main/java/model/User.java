package model;

public class User {
    private final String username;
    private final String role;
    private final String hashedPassword;

    public User(String username, String hashedPassword, String role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "'}";
    }

    public static class Payment {
        private String pid; // optional at creation
        private String type;
        private String category;
        private String counterPartyId;
        private long amount;
        private String paymentStatus;

        public Payment(String type, String category, String counterPartyId, long amount, String paymentStatus) {
            this.type = type;
            this.category = category;
            this.counterPartyId = counterPartyId;
            this.amount = amount;
            this.paymentStatus = paymentStatus;
        }

        // Getters and Setters
        public String getPid() { return pid; }
        public void setPid(String pid) { this.pid = pid; }

        public String getType() { return type; }
        public String getCategory() { return category; }
        public String getCounterPartyId() { return counterPartyId; }
        public long getAmount() { return amount; }
        public String getPaymentStatus() { return paymentStatus; }
    }
}
