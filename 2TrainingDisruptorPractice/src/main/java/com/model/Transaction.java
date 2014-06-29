package com.model;

import java.math.BigDecimal;
import java.util.Date;


public class Transaction {
	private String id;
    private Date date;
    private BigDecimal amount;
    private String accountnbr;
    private String type;

    public Transaction() {
    }

    public Transaction(String id, Date date, BigDecimal amount, String accountnbr, String type) {
    	this.id = id;
        this.date = date;
        this.amount = amount;
        this.accountnbr = accountnbr;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountnbr() {
        return accountnbr;
    }

    public void setAccountnbr(String accountnbr) {
        this.accountnbr = accountnbr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return String.format("[%s]  %s [%s] %s %s", id, date, type, accountnbr, amount);
    }
}
