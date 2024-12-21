package com.example.final_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.example.final_project.models.Transaction;

import java.util.List;

public class TransactionAdapter extends BaseAdapter {
    private Context context;
    private List<Transaction> transactions;

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.transaction_list_item, parent, false);
        }

        Transaction transaction = transactions.get(position);

        TextView transactionDate = convertView.findViewById(R.id.transactionDate);
        TextView transactionType = convertView.findViewById(R.id.transactionType);
        TextView transactionAmount = convertView.findViewById(R.id.transactionAmount);
        TextView fromAccountId = convertView.findViewById(R.id.fromAccountId);
        TextView toAccountId = convertView.findViewById(R.id.toAccountId);

        transactionDate.setText("Date: " + transaction.getTransactionDate());
        transactionType.setText("Type: " + transaction.getTransactionType());
        transactionAmount.setText("Amount: $" + String.format("%.2f", transaction.getAmount()));
        fromAccountId.setText("From: " + transaction.getFromAccountId());
        toAccountId.setText("To: " + transaction.getToAccountId());

        return convertView;
    }

}
