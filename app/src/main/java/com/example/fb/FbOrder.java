package com.example.fb;

/** Model Firebase: node 'orders' */
public class FbOrder {
    private String key;          // ORD1001...
    private String customerId;
    private String employeeId;
    private String orderDate;    // ISO string từ Firebase
    private String status;       // Completed | Shipping | Processing | Pending
    private double totalAmount;

    public FbOrder() {}

    public FbOrder(String key, String customerId, String employeeId,
                   String orderDate, String status, double totalAmount) {
        this.key = key;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public String getKey()                      { return key; }
    public void   setKey(String key)            { this.key = key; }
    public String getCustomerId()               { return customerId; }
    public void   setCustomerId(String c)       { this.customerId = c; }
    public String getEmployeeId()               { return employeeId; }
    public void   setEmployeeId(String e)       { this.employeeId = e; }
    public String getOrderDate()                { return orderDate; }
    public void   setOrderDate(String d)        { this.orderDate = d; }
    public String getStatus()                   { return status; }
    public void   setStatus(String s)           { this.status = s; }
    public double getTotalAmount()              { return totalAmount; }
    public void   setTotalAmount(double t)      { this.totalAmount = t; }
}
