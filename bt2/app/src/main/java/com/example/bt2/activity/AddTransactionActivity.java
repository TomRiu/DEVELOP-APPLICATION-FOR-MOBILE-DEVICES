package com.example.bt2.activity;

import android.content.Intent;
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
    private Button buttonAdd, buttonAddCategory;

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
        buttonAddCategory = findViewById(R.id.buttonAddCategory);

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
        // Set up add category button click listener
        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTransactionActivity.this, AddCategoryActivity.class);
                startActivityForResult(intent, 1); // Khởi chạy AddCategoryActivity với yêu cầu nhận kết quả
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Gọi lại danh sách danh mục
            boolean isIncome = radioThu.isChecked(); // Kiểm tra loại danh mục
            loadCategories(isIncome);
        }
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
}
