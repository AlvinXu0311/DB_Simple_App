package com.example.final_project;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.final_project.data.DatabaseHelper;
import com.example.final_project.models.Customer;

public class UserActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Fetch Customer ID from Intent
        String customerId = getIntent().getStringExtra("CUSTOMER_ID");
        if (customerId == null || customerId.isEmpty()) {
            finish(); // Exit if no Customer ID is provided
            return;
        }

// Fetch user information
        Customer customer = databaseHelper.getCustomerById(customerId);

// Populate the UI
        if (customer != null) {
            TextView userName = findViewById(R.id.user_name);
            TextView userDob = findViewById(R.id.user_dob);
            TextView userAddress = findViewById(R.id.user_address);
            TextView userEmail = findViewById(R.id.user_email);
            TextView userPhone = findViewById(R.id.user_phone);

            userName.setText(String.format("Name: %s %s", customer.getFirstName(), customer.getLastName()));
            userDob.setText(String.format("Date of Birth: %s", customer.getDateOfBirth()));
            userAddress.setText(String.format("Address: %s", customer.getAddress()));
            userEmail.setText(String.format("Email: %s", customer.getEmail()));
            userPhone.setText(String.format("Phone: %s", customer.getPhoneNumber()));
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
