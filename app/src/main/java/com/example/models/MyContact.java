package com.example.models;

import java.io.Serializable;

public class MyContact implements Serializable {
    private String name;
    private String phone;

    public MyContact() {
    }

    public MyContact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return this.name + "\n" + this.phone;
    }
}
