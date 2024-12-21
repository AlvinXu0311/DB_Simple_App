package com.example.final_project.models;

public class CreditCard {
    private String cardId;
    private double cardLimit;
    private double balance;
    private String issueDate;
    private String expiryDate;
    private String cardStatus;
    private double interestRate;
    private String customerId;

    public CreditCard(String cardId, double cardLimit, double balance, String issueDate, String expiryDate,
                      String cardStatus, double interestRate, String customerId) {
        this.cardId = cardId;
        this.cardLimit = cardLimit;
        this.balance = balance;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.cardStatus = cardStatus;
        this.interestRate = interestRate;
        this.customerId = customerId;
    }

    // Getters and Setters
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public double getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(double cardLimit) {
        this.cardLimit = cardLimit;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardId='" + cardId + '\'' +
                ", cardLimit=" + cardLimit +
                ", balance=" + balance +
                ", issueDate='" + issueDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", cardStatus='" + cardStatus + '\'' +
                ", interestRate=" + interestRate +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
