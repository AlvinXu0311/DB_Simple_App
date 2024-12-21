package com.example.final_project;

import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.data.DatabaseHelper;
import com.example.final_project.models.Transaction;

import java.util.List;

public class CheckingSavingsActivity extends AppCompatActivity {
    private static final String TAG = "CheckingSavingsActivity";

    private DatabaseHelper dbHelper;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_savings_activity);

        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        if (customerId == null) {
            Toast.makeText(this, "No customer ID provided.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);

        loadTotalBalance();
        loadRecentTransactions();

        Spinner monthSpinner = findViewById(R.id.month_spinner);

        findViewById(R.id.filterByMonthButton).setOnClickListener(v -> {
            int selectedMonth = monthSpinner.getSelectedItemPosition() + 1;
            filterTransactionsByMonth(selectedMonth, false);
        });

        findViewById(R.id.filterOver6MonthsButton).setOnClickListener(v -> filterTransactionsByMonth(0, true));

        findViewById(R.id.resetFilterButton).setOnClickListener(v -> loadRecentTransactions());
    }

    private void loadTotalBalance() {
        try {
            double totalBalance = dbHelper.getTotalBalance(customerId);
            TextView totalBalanceTextView = findViewById(R.id.totalBalanceTextView);
            String formattedBalance = String.format("Total Balance: $%.2f", totalBalance);
            totalBalanceTextView.setText(formattedBalance);
        } catch (Exception e) {
            Log.e(TAG, "Error fetching total balance: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to load total balance.", Toast.LENGTH_SHORT).show();
        }
    }
    private void filterTransactionsByMonth(int month, boolean isOver6Months) {
        try {
            List<Transaction> transactions;
            if (isOver6Months) {
                transactions = dbHelper.getTransactionsOlderThan6Months(customerId);
            } else {
                transactions = dbHelper.getTransactionsByMonth(customerId, month);
            }

            if (transactions.isEmpty()) {
                Toast.makeText(this, "No transactions found for the selected filter.", Toast.LENGTH_SHORT).show();
            } else {
                ListView transactionsListView = findViewById(R.id.transactionListView);
                TransactionAdapter adapter = new TransactionAdapter(this, transactions);
                transactionsListView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error filtering transactions: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to filter transactions.", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadRecentTransactions() {
        try {
            List<Transaction> transactions = dbHelper.getRecentTransactions(customerId);
            if (transactions.isEmpty()) {
                Toast.makeText(this, "No recent transactions available.", Toast.LENGTH_SHORT).show();
            } else {
                ListView transactionsListView = findViewById(R.id.transactionListView);
                TransactionAdapter adapter = new TransactionAdapter(this, transactions);
                transactionsListView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching transactions: " + e.getMessage(), e);
            Toast.makeText(this, "Failed to load transactions.", Toast.LENGTH_SHORT).show();
        }
    }
}