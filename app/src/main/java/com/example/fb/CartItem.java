package com.example.fb;

/** Model cho giỏ hàng local (SQLite) */
public class CartItem {
    private int    id;           // SQLite auto-increment PK
    private String productId;
    private String productName;
    private double price;
    private int    quantity;
    private String imageUrl;

    public CartItem() {}

    public CartItem(String productId, String productName,
                    double price, int quantity, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public int    getId()                        { return id; }
    public void   setId(int id)                  { this.id = id; }
    public String getProductId()                 { return productId; }
    public void   setProductId(String p)         { this.productId = p; }
    public String getProductName()               { return productName; }
    public void   setProductName(String n)       { this.productName = n; }
    public double getPrice()                     { return price; }
    public void   setPrice(double price)         { this.price = price; }
    public int    getQuantity()                  { return quantity; }
    public void   setQuantity(int quantity)      { this.quantity = quantity; }
    public String getImageUrl()                  { return imageUrl; }
    public void   setImageUrl(String u)          { this.imageUrl = u; }

    /** Thành tiền = đơn giá × số lượng */
    public double getSubTotal() { return price * quantity; }
}
