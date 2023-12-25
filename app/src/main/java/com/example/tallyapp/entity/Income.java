package com.example.tallyapp.entity;

import java.util.Date;

public class Income {
    private int userID;
    private int incomeTypeID;
    private double amount;
    private Date date;

    public Income() {
    }

    public Income(int userID, int incomeTypeID, double amount, Date date) {
        this.userID = userID;
        this.incomeTypeID = incomeTypeID;
        this.amount = amount;
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getIncomeTypeID() {
        return incomeTypeID;
    }

    public void setIncomeTypeID(int incomeTypeID) {
        this.incomeTypeID = incomeTypeID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
