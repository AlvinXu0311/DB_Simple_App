package com.example.final_project.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.final_project.models.Account;
import com.example.final_project.models.CreditCard;
import com.example.final_project.models.CreditCardTransaction;
import com.example.final_project.models.Customer;
import com.example.final_project.models.Loan;
import com.example.final_project.models.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Database Information
    private static final String DATABASE_NAME = "bank.db"; // Must match the file in assets
    private static final int DATABASE_VERSION = 1;

    // Path to the database in internal storage
    private final String databasePath;
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        // Dynamically get the database file path
        this.databasePath = context.getDatabasePath(DATABASE_NAME).getPath();
        Log.d(TAG, "Database path: " + databasePath);

        // Ensure the database is copied from assets if needed
        copyDatabaseIfNeeded();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create tables here since we are using a preloaded database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if necessary
        Log.d(TAG, "Database upgraded from version " + oldVersion + " to " + newVersion);
    }

    private void copyDatabaseIfNeeded() {
        try {
            // Get the full path to the database file
            File databaseFile = new File(databasePath);

            // Force delete the database if it exists
            if (databaseFile.exists()) {
                Log.d(TAG, "Deleting existing database.");
                if (!databaseFile.delete()) {
                    Log.e(TAG, "Failed to delete existing database.");
                    return;
                }
            }

            // Ensure the parent directory exists
            File databaseDir = databaseFile.getParentFile();
            if (!databaseDir.exists()) {
                if (databaseDir.mkdirs()) {
                    Log.d(TAG, "Database directory created.");
                } else {
                    Log.e(TAG, "Failed to create database directory.");
                    return;
                }
            }

            // Copy the database from assets
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            OutputStream outputStream = new FileOutputStream(databaseFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.d(TAG, "Database copied successfully to: " + databasePath);
        } catch (Exception e) {
            Log.e(TAG, "Error copying database: " + e.getMessage(), e);
        }
    }

    public List<String> getAllCustomers() {
        List<String> customerNames = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            String query = "SELECT Customer_ID, First_Name, Last_Name FROM Customer";
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    String customerName = cursor.getString(cursor.getColumnIndexOrThrow("First_Name")) + " " +
                            cursor.getString(cursor.getColumnIndexOrThrow("Last_Name")) +
                            " (" + cursor.getString(cursor.getColumnIndexOrThrow("Customer_ID")) + ")";
                    customerNames.add(customerName);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching customers: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return customerNames;
    }

    public List<Account> getAllAccounts(String customerId) {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM Account WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                do {
                    Account account = new Account(
                            cursor.getString(cursor.getColumnIndexOrThrow("Account_ID")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Account_Type")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Balance")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Open_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Customer_ID"))
                    );
                    accounts.add(account);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching accounts: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return accounts;
    }

    // Fetch all credit cards for a specific customer
    public List<CreditCard> getAllCreditCards(String customerId) {
        List<CreditCard> creditCards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM Credit_Card WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                do {
                    CreditCard creditCard = new CreditCard(
                            cursor.getString(cursor.getColumnIndexOrThrow("Card_ID")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Card_Limit")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Balance")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Issue_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Expiry_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Card_Status")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Interest_Rate")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Customer_ID"))
                    );
                    creditCards.add(creditCard);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching credit cards: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return creditCards;
    }
    public List<Transaction> getRecentTransactions(String customerId) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM Transactions WHERE From_Account_ID IN (SELECT Account_ID FROM Account WHERE Customer_ID = ?) ORDER BY Transaction_Date DESC LIMIT 10";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{customerId});
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction(
                            cursor.getString(cursor.getColumnIndexOrThrow("Transaction_ID")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Type")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Amount")),
                            cursor.getString(cursor.getColumnIndexOrThrow("From_Account_ID")),
                            cursor.getString(cursor.getColumnIndexOrThrow("To_Account_ID"))
                    );
                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching transactions: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return transactions;
    }
    public List<Loan> getAllLoans(String customerId) {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            // Include Account_ID in the query
            String query = "SELECT Loan_ID, Loan_Amount, Interest_Rate, Start_Date, End_Date, Customer_ID, Account_ID FROM Loan WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                do {
                    // Map each row to a Loan object
                    Loan loan = new Loan(
                            cursor.getString(cursor.getColumnIndexOrThrow("Loan_ID")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Loan_Amount")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Interest_Rate")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Start_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("End_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Customer_ID")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Account_ID"))
                    );
                    loans.add(loan);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching loans: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return loans;
    }
    public List<Loan> getLoansByCustomerId(String customerId) {
        List<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            // Include Account_ID in the query
            String query = "SELECT Loan_ID, Loan_Amount, Interest_Rate, Start_Date, End_Date, Customer_ID, Account_ID FROM Loan WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                do {
                    // Map each row to a Loan object
                    Loan loan = new Loan(
                            cursor.getString(cursor.getColumnIndexOrThrow("Loan_ID")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Loan_Amount")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Interest_Rate")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Start_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("End_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Customer_ID")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Account_ID")) // Fetch Account_ID
                    );
                    loans.add(loan);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching loans: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return loans;
    }

    public double getTotalLoanAmountByCustomerId(String customerId) {
        double totalLoanAmount = 0.0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(Loan_Amount) AS TotalAmount FROM Loan WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                totalLoanAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalAmount"));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error calculating total loan amount: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return totalLoanAmount;
    }


    // Fetch total balance for a specific customer
    public double getTotalBalance(String customerId) {
        double totalBalance = 0.0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(Balance) AS TotalBalance FROM Account WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                totalBalance = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalBalance"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching total balance: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return totalBalance;
    }

//    public List<String> getCardIdsByCustomerId(String customerId) {
//        List<String> cardIds = new ArrayList<>();
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = null;
//
//        try {
//            String query = "SELECT Card_ID FROM Credit_Card WHERE Customer_ID = ?";
//            cursor = db.rawQuery(query, new String[]{customerId});
//
//            if (cursor.moveToFirst()) {
//                do {
//                    String cardId = cursor.getString(cursor.getColumnIndexOrThrow("Card_ID"));
//                    cardIds.add(cardId);
//                } while (cursor.moveToNext());
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Error fetching card IDs: " + e.getMessage(), e);
//        } finally {
//            if (cursor != null) cursor.close();
//            db.close();
//        }
//
//        return cardIds;
//    }
    public List<CreditCard> getCreditCardsByCustomerId(String customerId) {
        List<CreditCard> creditCards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            // Include Balance in the query
            String query = "SELECT Card_ID, Card_Limit, Balance, Issue_Date, Expiry_Date, Card_Status, Interest_Rate, Customer_ID FROM Credit_Card WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                do {
                    // Map each row to a CreditCard object
                    CreditCard creditCard = new CreditCard(
                            cursor.getString(cursor.getColumnIndexOrThrow("Card_ID")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Card_Limit")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Balance")),  // Fetch the balance
                            cursor.getString(cursor.getColumnIndexOrThrow("Issue_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Expiry_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Card_Status")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Interest_Rate")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Customer_ID"))
                    );
                    creditCards.add(creditCard);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching credit cards: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return creditCards;
    }

    public List<CreditCardTransaction> getCreditCardTransactions(String cardId) {
        List<CreditCardTransaction> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to fetch transactions for the given Card_ID
            String query = "SELECT * FROM Credit_Card_Transaction WHERE Card_ID = ? ORDER BY Transaction_Date DESC";
            cursor = db.rawQuery(query, new String[]{cardId});

            if (cursor.moveToFirst()) {
                do {
                    // Map each row to a CreditCardTransaction object
                    CreditCardTransaction transaction = new CreditCardTransaction(
                            cursor.getString(cursor.getColumnIndexOrThrow("CC_Transaction_ID")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Date")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Type")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("Amount")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Merchant_Details")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                            cursor.getString(cursor.getColumnIndexOrThrow("Card_ID"))
                    );
                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching transactions: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return transactions;
    }
    public Customer getCustomerById(String customerId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        Customer customer = null;

        try {
            String query = "SELECT Customer_ID, First_Name, Last_Name, Date_of_Birth, Address, Email, Phone_Number FROM Customer WHERE Customer_ID = ?";
            cursor = db.rawQuery(query, new String[]{customerId});

            if (cursor.moveToFirst()) {
                customer = new Customer(
                        cursor.getString(cursor.getColumnIndexOrThrow("Customer_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("First_Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Last_Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date_of_Birth")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Phone_Number"))
                );
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching customer: " + e.getMessage(), e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return customer;
    }
    public List<Transaction> getTransactionsByMonth(String customerId, int month) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM Transactions WHERE From_Account_ID IN (SELECT Account_ID FROM Account WHERE Customer_ID = ?) AND strftime('%m', Transaction_Date) = ?";
            cursor = db.rawQuery(query, new String[]{customerId, String.format("%02d", month)});

            while (cursor.moveToNext()) {
                transactions.add(new Transaction(
                        cursor.getString(cursor.getColumnIndexOrThrow("Transaction_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Type")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Amount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("From_Account_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("To_Account_ID"))
                ));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return transactions;
    }

    public List<Transaction> getTransactionsOlderThan6Months(String customerId) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM Transactions WHERE From_Account_ID IN (SELECT Account_ID FROM Account WHERE Customer_ID = ?) AND Transaction_Date <= DATE('now', '-6 months')";
            cursor = db.rawQuery(query, new String[]{customerId});

            while (cursor.moveToNext()) {
                transactions.add(new Transaction(
                        cursor.getString(cursor.getColumnIndexOrThrow("Transaction_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Transaction_Type")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Amount")),
                        cursor.getString(cursor.getColumnIndexOrThrow("From_Account_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("To_Account_ID"))
                ));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return transactions;
    }

}


