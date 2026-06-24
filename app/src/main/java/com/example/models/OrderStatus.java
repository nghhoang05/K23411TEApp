package com.example.models;

public enum OrderStatus {
    ALL("All"),
    COMPLETED("Completed"),
    NOT_PAYMENT("Not Payment"),
    ON_LOGISTIC("On Logistic"),
    COMPLAINT("Complaint");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
