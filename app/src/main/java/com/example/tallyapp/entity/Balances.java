package com.example.tallyapp.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Balances {
    private int id;
    private String RecordType;
    private String TypeName;
    private BigDecimal Amount;
    private Date RecordDate;

    public Balances(int id, String recordType, String typeName, BigDecimal amount, Date recordDate) {
        this.id = id;
        RecordType = recordType;
        TypeName = typeName;
        Amount = amount;
        RecordDate = recordDate;
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
