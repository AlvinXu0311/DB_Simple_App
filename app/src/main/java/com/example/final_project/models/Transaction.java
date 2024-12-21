package com.example.final_project.models;

public class Transaction {
    private String transactionId;
    private String transactionDate;
    private String transactionType;
    private double amount;
    private String fromAccountId;
    private String toAccountId;

    public Transaction(String transactionId, String transactionDate, String transactionType, double amount, String fromAccountId, String toAccountId) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.amount = amount;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    // Setters (optional if needed)
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    @Override
    public String toString() {
        return "Transaction ID: " + transactionId +
                "\nDate: " + transactionDate +
                "\nType: " + transactionType +
                "\nAmount: $" + String.format("%.2f", amount) +
                "\nFrom Account: " + fromAccountId +
                "\nTo Account: " + toAccountId + "\n";
    }
}
