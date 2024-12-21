package com.example.final_project.models;

public class Account {
    private String accountId;
    private String accountType;
    private double balance;
    private String openDate;
    private String customerId;

    public Account(String accountId, String accountType, double balance, String openDate, String customerId) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.balance = balance;
        this.openDate = openDate;
        this.customerId = customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public String getOpenDate() {
        return openDate;
    }

    public String getCustomerId() {
        return customerId;
    }
}
