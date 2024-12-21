package com.example.final_project;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.final_project.data.DatabaseHelper;
import com.example.final_project.models.Loan;
import java.util.List;

public class LoansActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loans_activity);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Fetch loans for a specific customer (replace with actual customer ID)
        String customerId = getIntent().getStringExtra("CUSTOMER_ID");
        List<Loan> loans = databaseHelper.getLoansByCustomerId(customerId);

        // Populate the page
        populateLoansSummary(customerId);
    }
    /**
     * Dynamically add a loan card to the Loans List UI.
     */
    /**
     * Dynamically add a loan card to the Loans List UI.
     */
    private void addLoanToUI(Loan loan, LinearLayout loansList) {
        // Inflate loan card layout
        LinearLayout loanCard = (LinearLayout) getLayoutInflater().inflate(R.layout.loan_card_template, loansList, false);

        // Populate loan data
        TextView loanId = loanCard.findViewById(R.id.loan_id);
        TextView loanAmount = loanCard.findViewById(R.id.loan_amount);
        TextView loanInterestRate = loanCard.findViewById(R.id.loan_interest_rate);
        TextView loanDates = loanCard.findViewById(R.id.loan_dates);

        loanId.setText("Loan ID: " + loan.getLoanId());
        loanAmount.setText("Loan Amount: $" + String.format("%,.2f", loan.getLoanAmount()));
        loanInterestRate.setText("Interest Rate: " + loan.getInterestRate() + "%");
        loanDates.setText("Start Date: " + loan.getStartDate() + "\nEnd Date: " + loan.getEndDate());

        // Add the card to the loans list
        loansList.addView(loanCard);
    }
    private void populateLoansSummary(String customerId) {
        // Get total loan amount
        double totalLoanAmountValue = databaseHelper.getTotalLoanAmountByCustomerId(customerId);

        // Get UI components
        TextView totalLoanAmount = findViewById(R.id.total_loan_amount);
        TextView loansListTitle = findViewById(R.id.loans_list_title);
        LinearLayout loansList = findViewById(R.id.loans_list);
        TextView noLoansMessage = findViewById(R.id.no_loans_message);

        // Update total loan amount in the UI
        totalLoanAmount.setText(String.format("Total Loan Amount: $%,.2f", totalLoanAmountValue));

        // Fetch loans for the customer
        List<Loan> loans = databaseHelper.getLoansByCustomerId(customerId);

        if (loans != null && !loans.isEmpty()) {
            // Show the title and populate the loans list
            loansListTitle.setVisibility(View.VISIBLE);
            noLoansMessage.setVisibility(View.GONE);

            for (Loan loan : loans) {
                addLoanToUI(loan, loansList);
            }
        } else {
            // Hide the loans list and display no loans message
            loansList.setVisibility(View.GONE);
            loansListTitle.setVisibility(View.GONE);
            noLoansMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
