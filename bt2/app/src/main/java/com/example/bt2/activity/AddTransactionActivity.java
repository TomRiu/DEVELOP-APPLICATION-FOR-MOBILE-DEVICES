package com.example.bt2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.R;
import com.example.bt2.adapter.CategoryAdapter;
import com.example.bt2.dao.CategoryDAO;
import com.example.bt2.dao.DBHelper;
import com.example.bt2.dao.TransactionDAO;
import com.example.bt2.model.CategoryInOut;
import com.example.bt2.model.Transaction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

    private RadioButton radioThu, radioChi;
    private Spinner spinnerCategory;
    private EditText editTextAmount, editTextNote;
    private DatePicker datePicker;
    private Button buttonAdd;

    private CategoryDAO categoryDAO;
    private TransactionDAO transactionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);

        // Initialize views
        radioThu = findViewById(R.id.radioThu);
        radioChi = findViewById(R.id.radioChi);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextAmount = findViewById(R.id.editTextAmount);
        datePicker = findViewById(R.id.datePicker);
        editTextNote = findViewById(R.id.editTextNote);
        buttonAdd = findViewById(R.id.buttonAdd);

        // Initialize DAOs
        DBHelper dbHelper = new DBHelper(this);
        categoryDAO = new CategoryDAO(dbHelper.getReadableDatabase());
        transactionDAO = new TransactionDAO(dbHelper.getWritableDatabase());

        // Set up category spinner
        setupCategorySpinner();

        // Set up add button click listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTransaction();
            }
        });
    }

    private void setupCategorySpinner() {
        radioThu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadCategories(true);
            }
        });

        radioChi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loadCategories(false);
            }
        });

        // Default to "Thu" categories
        radioThu.setChecked(true);
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

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();

            Transaction transaction = new Transaction();
            transaction.setCategory(selectedCategoryInOut);
            transaction.setAmount(amount);
            transaction.setDay(date);
            transaction.setNote(note);

            boolean success = transactionDAO.add(transaction);

            if (success) {
                Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add transaction", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}