package com.example.models;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class Employee implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String birthPlace;

    public String getBirthplace() {
        return birthPlace;
    }

    public void setBirthplace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Employee(String id, String name, String phone, String birthplace) {
        this(id, name, phone); // Gọi constructor đầy đủ đối số (id, name, phone)
        this.birthPlace = birthPlace;
    }

    // Constructor không đối số
    public Employee() {
    }

    // Constructor đầy đủ đối số
    public Employee(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Getter và Setter cho ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter và Setter cho Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho Phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Hàm toString để kiểm thử
    @NonNull
    @Override
    public String toString() {
        return id + " - " + name + " - " + phone;
    }
}
