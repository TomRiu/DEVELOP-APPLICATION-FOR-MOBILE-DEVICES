package com.example.bt2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.R;
import com.example.bt2.adapter.TransactionAdapter;
import com.example.bt2.dao.DBHelper;
import com.example.bt2.dao.TransactionDAO;
import com.example.bt2.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewDate;
    private Button buttonTongThu, buttonTongChi, buttonThemGiaoDich;
    private ListView listViewTransactions;
    private TextView textViewTransaction;

    private TransactionDAO transactionDAO;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initialize views
        textViewDate = findViewById(R.id.textDate);
        buttonTongThu = findViewById(R.id.buttonTongThu);
        buttonTongChi = findViewById(R.id.buttonTongChi);
        listViewTransactions = findViewById(R.id.listViewTransactions);
        buttonThemGiaoDich = findViewById(R.id.buttonThemGiaoDich);
        textViewTransaction = findViewById(R.id.textCountTransaction);
        buttonThemGiaoDich = findViewById(R.id.buttonThemGiaoDich);

        // Set current date
        setCurrentDate();

        // Initialize DAO
        DBHelper dbHelper = new DBHelper(this);
        transactionDAO = new TransactionDAO(dbHelper.getReadableDatabase());

        // Load transactions
        loadTransactions();

        // Set up button click listeners
        setupButtonListeners();
    }

    private void setCurrentDate() {
        if (textViewDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());
            textViewDate.setText(currentDate);
        }
    }

    private void loadTransactions() {
        List<Transaction> transactions = transactionDAO.search(new Date());
        transactionAdapter = new TransactionAdapter(this, transactions);
        listViewTransactions.setAdapter(transactionAdapter);
        if (textViewTransaction != null) {
            textViewTransaction.setText(String.format(Locale.getDefault(), "%d giao dịch", transactions.size()));
        }
    }

    private void setupButtonListeners() {
        buttonTongThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalIncome = transactionDAO.getTotalIncome(new Date());
                int incomeCount = transactionDAO.getIncomeCount(new Date());
                buttonTongThu.setText(String.format(Locale.getDefault(), "Tổng thu: %.2f (%d lần)", totalIncome, incomeCount));
                buttonTongThu.setTextColor(Color.GREEN);
            }
        });

        buttonTongChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalExpense = transactionDAO.getTotalExpense(new Date());
                int expenseCount = transactionDAO.getExpenseCount(new Date());
                buttonTongChi.setText(String.format(Locale.getDefault(), "Tổng chi: %.2f (%d lần)", totalExpense, expenseCount));
                buttonTongChi.setTextColor(Color.RED);
            }
        });

        buttonThemGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddTransactionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload transactions when returning to this activity
        loadTransactions();
    }
}