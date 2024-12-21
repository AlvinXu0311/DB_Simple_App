package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.data.DatabaseHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner userSpinner;
    private Button nextButton;
    private String selectedCustomerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        userSpinner = findViewById(R.id.userSpinner);
        nextButton = findViewById(R.id.nextButton);

        // Load customers from the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<String> customers = dbHelper.getAllCustomers();

        // Check if the list is empty
        if (customers.isEmpty()) {
            Toast.makeText(this, "No customers found in the database", Toast.LENGTH_SHORT).show();
            return;
        }

        // Populate the spinner with customer names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, customers);
        userSpinner.setAdapter(adapter);

        // Spinner item selection listener
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                // Extract the Customer_ID from the selected item (e.g., "John Doe (CUST001)")
                String selectedCustomer = customers.get(position);
                selectedCustomerId = selectedCustomer.substring(selectedCustomer.lastIndexOf("(") + 1, selectedCustomer.lastIndexOf(")"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCustomerId = null;
            }
        });

        // Next button click listener
        nextButton.setOnClickListener(v -> {
            if (selectedCustomerId != null) {
                // Pass the selected Customer_ID to the SummaryActivity
                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                intent.putExtra("CUSTOMER_ID", selectedCustomerId); // Pass Customer_ID
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please select a user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
