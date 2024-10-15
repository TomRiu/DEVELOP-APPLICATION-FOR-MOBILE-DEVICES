package com.example.bt2.activity;

import android.app.AlertDialog;
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
import com.example.bt2.view.CalendarViewPager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewDate;
    private Button buttonTongThu, buttonTongChi, buttonThemGiaoDich, buttonShowCalendar;
    private ListView listViewTransactions;
    private TextView textViewTransaction;

    private TransactionDAO transactionDAO;
    private TransactionAdapter transactionAdapter;
    private SimpleDateFormat dateFormat;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Intent intent = getIntent();
        long dateMillis = intent.getLongExtra("selected_date", -1);
        if (dateMillis != -1) {
            currentDate = new Date(dateMillis);
        } else {
            currentDate = new Date();
        }

        initializeViews();
        initializeDAO();
        setCurrentDate();
        loadTransactions();
        setupButtonListeners();
    }

    private void initializeViews() {
        textViewDate = findViewById(R.id.textDate);
        buttonTongThu = findViewById(R.id.buttonTongThu);
        buttonTongChi = findViewById(R.id.buttonTongChi);
        listViewTransactions = findViewById(R.id.listViewTransactions);
        buttonThemGiaoDich = findViewById(R.id.buttonThemGiaoDich);
        textViewTransaction = findViewById(R.id.textCountTransaction);
        buttonShowCalendar = findViewById(R.id.buttonShowCalendar);
    }

    private void initializeDAO() {
        DBHelper dbHelper = new DBHelper(this);
        transactionDAO = new TransactionDAO(dbHelper.getReadableDatabase());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    private void setCurrentDate() {
        String currentDateStr = dateFormat.format(currentDate);
        textViewDate.setText(currentDateStr);
    }

    private void loadTransactions() {
        List<Transaction> transactions = transactionDAO.search(currentDate);
        transactionAdapter = new TransactionAdapter(this, transactions);
        listViewTransactions.setAdapter(transactionAdapter);
        textViewTransaction.setText(String.format(Locale.getDefault(), "%d giao dịch", transactions.size()));
    }

    private void setupButtonListeners() {
        buttonTongThu.setOnClickListener(v -> showTotalIncome());
        buttonTongChi.setOnClickListener(v -> showTotalExpense());
        buttonThemGiaoDich.setOnClickListener(v -> openAddTransactionActivity());
        buttonShowCalendar.setOnClickListener(v -> showCalendarDialog());
    }

    private void showTotalIncome() {
        double totalIncome = transactionDAO.getTotalIncome(currentDate);
        int incomeCount = transactionDAO.getIncomeCount(currentDate);
        buttonTongThu.setText(String.format(Locale.getDefault(), "Tổng thu: %.2f (%d lần)", totalIncome, incomeCount));
        buttonTongThu.setTextColor(Color.GREEN);
    }

    private void showTotalExpense() {
        double totalExpense = transactionDAO.getTotalExpense(currentDate);
        int expenseCount = transactionDAO.getExpenseCount(currentDate);
        buttonTongChi.setText(String.format(Locale.getDefault(), "Tổng chi: %.2f (%d lần)", totalExpense, expenseCount));
        buttonTongChi.setTextColor(Color.RED);
    }

    private void openAddTransactionActivity() {
        Intent intent = new Intent(HomeActivity.this, AddTransactionActivity.class);
        startActivity(intent);
    }

    private void showCalendarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View calendarView = getLayoutInflater().inflate(R.layout.dialog_calendar, null);
        CalendarViewPager calendarViewPager = calendarView.findViewById(R.id.calendarViewPager);
        TextView monthYearText = calendarView.findViewById(R.id.monthYearText);

        calendarViewPager.setOnDateSelectedListener(this::onDateSelected);
        calendarViewPager.setOnMonthChangedListener((year, month) -> {
            String monthYearStr = new SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    .format(new Date(year - 1900, month, 1));
            monthYearText.setText(monthYearStr);
        });

        builder.setView(calendarView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onDateSelected(Date date) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("selected_date", date.getTime());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTransactions();
    }
}
