package com.example.final_project.models;

public class CreditCardTransaction {
    private String transactionId;        // CC_Transaction_ID
    private String transactionDate;      // Transaction_Date
    private String transactionType;      // Transaction_Type
    private double amount;               // Amount
    private String merchantDetails;      // Merchant_Details
    private String description;          // Description
    private String cardId;               // Card_ID

    // Constructor
    public CreditCardTransaction(String transactionId, String transactionDate, String transactionType,
                                 double amount, String merchantDetails, String description, String cardId) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.amount = amount;
        this.merchantDetails = merchantDetails;
        this.description = description;
        this.cardId = cardId;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMerchantDetails() {
        return merchantDetails;
    }

    public void setMerchantDetails(String merchantDetails) {
        this.merchantDetails = merchantDetails;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return "CreditCardTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", merchantDetails='" + merchantDetails + '\'' +
                ", description='" + description + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
