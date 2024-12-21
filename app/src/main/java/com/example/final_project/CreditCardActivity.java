package com.example.final_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.data.DatabaseHelper;
import com.example.final_project.models.CreditCard;
import com.example.final_project.models.CreditCardTransaction;

import java.util.ArrayList;
import java.util.List;

public class CreditCardActivity extends AppCompatActivity {
    private static final String TAG = "CreditCardActivity";

    private DatabaseHelper databaseHelper;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_activity);

        // Get the Customer ID from Intent
        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        if (customerId == null || customerId.isEmpty()) {
            Log.e(TAG, "Customer ID is missing");
            finish(); // Exit the activity if Customer ID is not provided
            return;
        }

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Fetch Credit Cards for the Customer ID
        List<CreditCard> creditCards = databaseHelper.getCreditCardsByCustomerId(customerId);
        if (creditCards.isEmpty()) {
            Log.e(TAG, "No credit cards found for the given Customer ID");
            finish(); // Exit if no cards are associated with the customer
            return;
        }

        // Use the first active card (or prompt user to choose)
        CreditCard activeCard = null;
        for (CreditCard card : creditCards) {
            if ("Active".equalsIgnoreCase(card.getCardStatus())) {
                activeCard = card;
                break;
            }
        }

        if (activeCard == null) {
            Log.e(TAG, "No active cards found for the given Customer ID");
            finish();
            return;
        }

        Log.d(TAG, "Using Card ID: " + activeCard.getCardId());

        // Fetch transactions for the selected card
        List<CreditCardTransaction> transactions = databaseHelper.getCreditCardTransactions(activeCard.getCardId());

        // Display the card balance
        TextView balanceTextView = findViewById(R.id.credit_card_balance);
        balanceTextView.setText(String.format("Balance: $%.2f", activeCard.getBalance()));

        // Update the transactions list in the UI
        ListView transactionsListView = findViewById(R.id.recent_credit_card_transactions);
        updateTransactionsList(transactionsListView, transactions);
    }
    /**
     * Updates the ListView with the list of Credit Card Transactions.
     */
    private void updateTransactionsList(ListView transactionsListView, List<CreditCardTransaction> transactions) {
        // Prepare a list of descriptions to display in the ListView
        List<String> transactionDescriptions = new ArrayList<>();
        for (CreditCardTransaction transaction : transactions) {
            String description = String.format(
                    "%s - $%.2f\n%s\n%s\nDate: %s",
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getMerchantDetails(),
                    transaction.getDescription(),
                    transaction.getTransactionDate()
            );
            transactionDescriptions.add(description);
        }

        // Set up the adapter and attach it to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                transactionDescriptions
        );
        transactionsListView.setAdapter(adapter);
    }
}
