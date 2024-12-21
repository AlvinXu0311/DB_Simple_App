package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.data.DatabaseHelper;
import com.example.final_project.models.Account;
import com.example.final_project.models.CreditCard;
import com.example.final_project.models.Loan;

import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    private static final String TAG = "SummaryActivity";

    private DatabaseHelper dbHelper;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Get the customer ID passed from MainActivity
        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        if (customerId == null) {
            Toast.makeText(this, "No customer ID provided.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no customer ID is provided
            return;
        }

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Load the summary data
        loadSummaryData(customerId);
        // Set click listeners for sections

        // Button to navigate to the User Page
        Button goToUserPageButton = findViewById(R.id.goToUserPageButton);
        goToUserPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass Customer ID if needed
                Intent intent = new Intent(SummaryActivity.this, UserActivity.class);
                intent.putExtra("CUSTOMER_ID", customerId); // Pass customer ID if needed
                startActivity(intent);
            }
        });

        findViewById(R.id.accountsTextView).setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, CheckingSavingsActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId); // Pass customer ID to the next activity
            startActivity(intent);
        });

        findViewById(R.id.creditCardTextView).setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, CreditCardActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId); // Pass customer ID to the next activity
            startActivity(intent);
        });

        findViewById(R.id.loanDetailsTextView).setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, LoansActivity.class);
            intent.putExtra("CUSTOMER_ID", customerId); // Pass customer ID to the next activity
            startActivity(intent);
        });

        // Set up the return to first page button
        Button returnToFirstPageButton = findViewById(R.id.returnToFirstPageButton);
        returnToFirstPageButton.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close this activity to remove it from the back stack
        });
    }

    private void loadSummaryData(String customerId) {
        // Fetch total balance for the user
        try {
            double totalBalance = dbHelper.getTotalBalance(customerId);
            TextView totalBalanceTextView = findViewById(R.id.totalBalance);
            totalBalanceTextView.setText(String.format("Total Balance: $%.2f", totalBalance));
        } catch (Exception e) {
            Log.e(TAG, "Error fetching total balance: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to load total balance.", Toast.LENGTH_SHORT).show();
        }

        // Fetch and display accounts for the user
        try {
            List<Account> accounts = dbHelper.getAllAccounts(customerId);
            StringBuilder accountInfo = new StringBuilder();
            for (Account account : accounts) {
                accountInfo.append("Account: ").append(account.getAccountId())
                        .append("\nType: ").append(account.getAccountType())
                        .append("\nBalance: $").append(String.format("%.2f", account.getBalance()))
                        .append("\n\n");
            }

            TextView accountsTextView = findViewById(R.id.accountsTextView);
            accountsTextView.setText(accountInfo.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error fetching accounts: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to load account details.", Toast.LENGTH_SHORT).show();
        }

        // Fetch and display loan details for the user
        try {
            List<Loan> loans = dbHelper.getAllLoans(customerId);
            StringBuilder loanDetailsBuilder = new StringBuilder();
            for (Loan loan : loans) {
                loanDetailsBuilder.append("Loan ID: ").append(loan.getLoanId())
                        .append("\nAmount: $").append(String.format("%.2f", loan.getLoanAmount()))
                        .append("\nInterest Rate: ").append(String.format("%.2f", loan.getInterestRate())).append("%")
                        .append("\n\n");
            }

            TextView loanDetailsTextView = findViewById(R.id.loanDetailsTextView);
            if (loans.isEmpty()) {
                loanDetailsTextView.setText("No loan details available.");
            } else {
                loanDetailsTextView.setText(loanDetailsBuilder.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching loan details: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to load loan details.", Toast.LENGTH_SHORT).show();
        }

        // Fetch and display credit card details for the user
        try {
            List<CreditCard> creditCards = dbHelper.getAllCreditCards(customerId);
            StringBuilder creditCardInfo = new StringBuilder();
            for (CreditCard creditCard : creditCards) {
                creditCardInfo.append("Card : ").append(creditCard.getCardId())
                        .append("\nBalance: $").append(String.format("%.2f", creditCard.getBalance()))
                        .append("\n\n");
            }

            TextView creditCardTextView = findViewById(R.id.creditCardTextView);
            if (creditCards.isEmpty()) {
                creditCardTextView.setText("No credit card details available.");
            } else {
                creditCardTextView.setText(creditCardInfo.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching credit card details: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to load credit card details.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
