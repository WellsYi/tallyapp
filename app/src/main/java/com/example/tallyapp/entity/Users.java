package com.example.tallyapp.entity;

import androidx.annotation.NonNull;

public class Users {
    private final String username;
    private final String password;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public String toString() {
        return username;
    }
}
