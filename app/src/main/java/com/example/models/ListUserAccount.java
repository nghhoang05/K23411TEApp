package com.example.models;

import java.util.ArrayList;

public class ListUserAccount {
    public static ArrayList<UserAccount> getUserAccounts() {
        ArrayList<UserAccount> database=new ArrayList<>();
        database.add(new UserAccount("admin", "123", "Administrator", "Nguyen Huy Hoang", true));
        database.add(new UserAccount("employee", "123", "Employee", "Nguyen Van A", true));
        database.add(new UserAccount("employee2", "123", "Reporter", "Nguyen Van B", true));
        return database;
    }
    public static UserAccount Login(String username, String password) {
        //Step1: Query database
        ArrayList<UserAccount> database=getUserAccounts();
        //Step 2: Check username and password
        for (UserAccount acc:database) {
            if (acc.getUsername().equals(username) && acc.getPassword().equals(password)) {
                return acc;
            }
        }
        return null;
    }
}
