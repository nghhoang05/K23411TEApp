package com.example.fb;

import java.io.Serializable;

/** Model Firebase: node 'products' */
public class FbProduct implements Serializable {
    private String key;          // PROD001...
    private String productName;
    private String categoryId;
    private double price;
    private long   stock;
    private String imageUrl;
    private boolean active;      // maps to isActive in Firebase JSON

    public FbProduct() {}

    public FbProduct(String key, String productName, String categoryId,
                     double price, long stock, String imageUrl, boolean active) {
        this.key = key;
        this.productName = productName;
        this.categoryId = categoryId;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.active = active;
    }

    public String  getKey()                      { return key; }
    public void    setKey(String key)            { this.key = key; }
    public String  getProductName()              { return productName; }
    public void    setProductName(String n)      { this.productName = n; }
    public String  getCategoryId()               { return categoryId; }
    public void    setCategoryId(String c)       { this.categoryId = c; }
    public double  getPrice()                    { return price; }
    public void    setPrice(double price)        { this.price = price; }
    public long    getStock()                    { return stock; }
    public void    setStock(long stock)          { this.stock = stock; }
    public String  getImageUrl()                 { return imageUrl; }
    public void    setImageUrl(String u)         { this.imageUrl = u; }
    public boolean isActive()                    { return active; }
    public void    setActive(boolean active)     { this.active = active; }
}
