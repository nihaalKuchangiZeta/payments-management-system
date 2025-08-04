package model;

public class Client {
    private String clientId;
    private String clientName;
    private String status;

    // Constructor
    public Client(String clientId, String clientName, String status) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.status = status;
    }

    // Getters
    public String getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}