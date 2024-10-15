package com.example.bt2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.R;
import com.example.bt2.adapter.CategoryAdapter;
import com.example.bt2.dao.CategoryDAO;
import com.example.bt2.dao.DBHelper;
import com.example.bt2.dao.DailyStatDAO;
import com.example.bt2.dao.TransactionDAO;
import com.example.bt2.model.CategoryInOut;
import com.example.bt2.model.Transaction;
import com.example.bt2.view.CalendarViewPager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTransactionActivity extends AppCompatActivity {

    private RadioButton radioThu, radioChi;
    private Spinner spinnerCategory;
    private EditText editTextAmount, editTextNote;
    private DatePicker datePicker;
    private Button buttonAdd, buttonAddCategory, buttonShowCalendar;
    private TextView textViewDate;

    private CategoryDAO categoryDAO;
    private TransactionDAO transactionDAO;

    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        initializeViews();
        initializeDAOs();
        setupListeners();
        setupInitialDate();
    }

    private void initializeViews() {
        radioThu = findViewById(R.id.radioThu);
        radioChi = findViewById(R.id.radioChi);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextAmount = findViewById(R.id.editTextAmount);
        datePicker = findViewById(R.id.datePicker);
        editTextNote = findViewById(R.id.editTextNote);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);
        textViewDate = findViewById(R.id.textViewDate);
    }

    private void initializeDAOs() {
        DBHelper dbHelper = new DBHelper(this);
        categoryDAO = new CategoryDAO(dbHelper.getReadableDatabase());
        transactionDAO = new TransactionDAO(dbHelper.getWritableDatabase());
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    private void setupListeners() {
        radioThu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) loadCategories(true);
        });

        radioChi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) loadCategories(false);
        });

        buttonAdd.setOnClickListener(v -> addTransaction());
        buttonAddCategory.setOnClickListener(v -> openAddCategoryActivity());

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> updateDisplayedDate());
    }

    private void setupInitialDate() {
        radioThu.setChecked(true);
        updateDisplayedDate();
    }

    private void updateDisplayedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        textViewDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void loadCategories(boolean isIncome) {
        List<CategoryInOut> categories = categoryDAO.searchByInOut(isIncome);
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        spinnerCategory.setAdapter(adapter);
    }

    private void addTransaction() {
        try {
            CategoryInOut selectedCategoryInOut = (CategoryInOut) spinnerCategory.getSelectedItem();
            double amount = Double.parseDouble(editTextAmount.getText().toString());
            String note = editTextNote.getText().toString();

            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            Date date = calendar.getTime();

            Transaction transaction = new Transaction();
            transaction.setCategory(selectedCategoryInOut);
            transaction.setAmount(amount);
            transaction.setDay(date);
            transaction.setNote(note);

            boolean success = transactionDAO.add(transaction);

            if (success) {
                Toast.makeText(this, "Giao dịch đã được thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm giao dịch thất bại", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số tiền hợp lệ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void openAddCategoryActivity() {
        Intent intent = new Intent(AddTransactionActivity.this, AddCategoryActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean isIncome = radioThu.isChecked();
            loadCategories(isIncome);
        }
    }
}
