package com.example.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model đại diện cho một liên hệ (Contact).
 * Annotation @IgnoreExtraProperties giúp Firebase không crash
 * khi server có thêm trường mới mà model chưa khai báo.
 */
@IgnoreExtraProperties
public class Contact {
    private String key;    // ID node trong Firebase (contact1, contact2,...)
    private String name;
    private String email;
    private String phone;

    /** Constructor rỗng bắt buộc cho Firebase Realtime Database deserialize */
    public Contact() {}

    public Contact(String key, String name, String email, String phone) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getKey()   { return key; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setKey(String key)     { this.key = key; }
    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}
