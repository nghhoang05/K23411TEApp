package com.example.fb;

/** Model Firebase: node 'orderDetails' */
public class FbOrderDetail {
    private String key;          // OD1001_1...
    private String orderId;
    private String productId;
    private long   quantity;
    private double unitPrice;

    public FbOrderDetail() {}

    public FbOrderDetail(String key, String orderId, String productId,
                         long quantity, double unitPrice) {
        this.key = key;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getKey()                      { return key; }
    public void   setKey(String key)            { this.key = key; }
    public String getOrderId()                  { return orderId; }
    public void   setOrderId(String o)          { this.orderId = o; }
    public String getProductId()                { return productId; }
    public void   setProductId(String p)        { this.productId = p; }
    public long   getQuantity()                 { return quantity; }
    public void   setQuantity(long q)           { this.quantity = q; }
    public double getUnitPrice()                { return unitPrice; }
    public void   setUnitPrice(double u)        { this.unitPrice = u; }
}
