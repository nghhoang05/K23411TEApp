package com.example.fb;

/** Model Firebase: node 'customers' */
public class FbCustomer {
    private String key;          // CUST001...
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public FbCustomer() {}

    public FbCustomer(String key, String fullName, String email, String phone, String address) {
        this.key = key;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getKey()                  { return key; }
    public void   setKey(String key)        { this.key = key; }
    public String getFullName()             { return fullName; }
    public void   setFullName(String n)     { this.fullName = n; }
    public String getEmail()               { return email; }
    public void   setEmail(String e)        { this.email = e; }
    public String getPhone()               { return phone; }
    public void   setPhone(String p)        { this.phone = p; }
    public String getAddress()             { return address; }
    public void   setAddress(String a)     { this.address = a; }
}
