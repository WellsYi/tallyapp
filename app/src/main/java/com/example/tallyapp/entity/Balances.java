package com.example.tallyapp.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Balances {
    private int id;
    private String RecordType;
    private String TypeName;
    private BigDecimal Amount;
    private Date RecordDate;
    private Double Percentage;

    public Double getPercentage() {
        return Percentage;
    }

    public void setPercentage(Double percentage) {
        Percentage = percentage;
    }

    public Balances(int id, String recordType, String typeName, BigDecimal amount, Date recordDate) {
        this.id = id;
        RecordType = recordType;
        TypeName = typeName;
        Amount = amount;
        RecordDate = recordDate;
    }

    public Balances(String typeName, BigDecimal amount) {
        TypeName = typeName;
        Amount = amount;
    }

    public Balances() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecordType() {
        return RecordType;
    }

    public void setRecordType(String recordType) {
        RecordType = recordType;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public Date getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(Date recordDate) {
        RecordDate = recordDate;
    }
}
