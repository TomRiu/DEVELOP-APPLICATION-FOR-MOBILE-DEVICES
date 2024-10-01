package com.example.bt2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bt2.R;
import com.example.bt2.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_transaction, parent, false);
        }

        Transaction transaction = getItem(position);

        TextView textViewCategory = convertView.findViewById(R.id.textViewCategory);
        TextView textViewAmount = convertView.findViewById(R.id.textViewAmount);
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);

        if (transaction != null) {
            textViewCategory.setText(transaction.getCategory().getCategory().getName());

            // Format amount with currency symbol and color
            String amountText = String.format(Locale.getDefault(), "%s%.2f",
                    transaction.getCategory().getIdInOut() == 1 ? "+" : "-",
                    Math.abs(transaction.getAmount()));
            textViewAmount.setText(amountText);

            // Set color based on transaction type
            if (transaction.getCategory().getIdInOut() == 1) { // Assuming 1 is for income
                textViewAmount.setTextColor(Color.GREEN);
            } else {
                textViewAmount.setTextColor(Color.RED);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            textViewDate.setText(dateFormat.format(transaction.getDay()));
        }

        return convertView;
    }
}