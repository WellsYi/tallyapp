package com.example.tallyapp.entity;

public class User {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Integer remember;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.remember = 0;
    }

    public User() {
        this.remember = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRemember() {
        return remember;
    }

    public void setRemember(Integer remember) {
        this.remember = remember;
    }
}
