package com.example.tallyapp.entity;

import java.util.Date;

public class Expense {
    private int id;
    private int userID;
    private int expenseTypeID;
    private double amount;

    public Expense(int id, int userID, int expenseTypeID, double amount, Date date) {
        this.id = id;
        this.userID = userID;
        this.expenseTypeID = expenseTypeID;
        this.amount = amount;
        this.date = date;
    }

    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Expense() {
    }

    public Expense(int userID, int expenseTypeID, double amount, Date date) {
        this.userID = userID;
        this.expenseTypeID = expenseTypeID;
        this.amount = amount;
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getExpenseTypeID() {
        return expenseTypeID;
    }

    public void setExpenseTypeID(int expenseTypeID) {
        this.expenseTypeID = expenseTypeID;
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
