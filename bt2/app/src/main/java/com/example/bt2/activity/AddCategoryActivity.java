package com.example.bt2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt2.R;
import com.example.bt2.adapter.CategoryAdapter;
import com.example.bt2.dao.CategoryDAO;
import com.example.bt2.dao.DBHelper;
import com.example.bt2.model.Category;
import com.example.bt2.model.CategoryInOut;

import java.util.List;

public class AddCategoryActivity extends AppCompatActivity {

    private RadioGroup radioGroupInOut;
    private RadioButton radioIn, radioOut;
    private Spinner spinnerCategories, spinnerIcons;
    private EditText editTextName;
    private Button buttonAdd;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Khởi tạo các view
        radioGroupInOut = findViewById(R.id.radioGroupInOut);
        radioIn = findViewById(R.id.radioIn);
        radioOut = findViewById(R.id.radioOut);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        spinnerIcons = findViewById(R.id.spinnerIcons);
        editTextName = findViewById(R.id.editTextName);
        buttonAdd = findViewById(R.id.buttonAdd);

        // Khởi tạo DAO
        DBHelper dbHelper = new DBHelper(this);
        categoryDAO = new CategoryDAO(dbHelper.getWritableDatabase());



        // Tải icon vào spinner
        loadIcons();

        // Thiết lập sự kiện cho RadioGroup
        radioGroupInOut.setOnCheckedChangeListener((group, checkedId) -> {
            boolean isIncome = checkedId == R.id.radioIn;
            loadCategories(isIncome);
        });

        // Thiết lập nút thêm danh mục
        buttonAdd.setOnClickListener(v -> addCategory());

        // Mặc định chọn "In" và tải danh mục
        radioIn.setChecked(true);
        loadCategories(true);
    }

    private void loadIcons() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_icons, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIcons.setAdapter(adapter);
    }

    private void loadCategories(boolean isIncome) {
        List<CategoryInOut> categories = categoryDAO.searchByInOut(isIncome);
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        spinnerCategories.setAdapter(adapter);
    }

    private void addCategory() {
        String name = editTextName.getText().toString().trim();
        boolean isIncome = radioIn.isChecked();
        String selectedIcon = spinnerIcons.getSelectedItem().toString(); // Lấy icon đã chọn /res/drawable/home.png
        Category selectedParent = ((CategoryInOut) spinnerCategories.getSelectedItem()).getCategory(); // Lấy danh mục cha đã chọn

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Category;
        Category category = new Category();
        category.setName(name);
        category.setIcon("@" + selectedIcon.substring(4).split("\\.")[0]); // @drawable/home
        category.setParent(selectedParent);

        // Thêm danh mục vào cơ sở dữ liệu
        long categoryId = categoryDAO.addCategory(category);

        if (categoryId > 0) {
            // Cập nhật ID cho đối tượng Category
            category.setId((int) categoryId);

            // Tạo đối tượng CategoryInOut
            CategoryInOut categoryInOut = new CategoryInOut();
            categoryInOut.setCategory(category);
            categoryInOut.setIdInOut(isIncome ? 1 : 2); // Giả sử 1 cho "In" và 2 cho "Out"

            // Thêm CategoryInOut vào cơ sở dữ liệu
            boolean success = categoryDAO.addCategoryInOut(categoryInOut); // Cần thêm phương thức này

            if (success) {
                Toast.makeText(this, "Danh mục đã được thêm thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Thiết lập kết quả thành công
                finish(); // Kết thúc hoạt động
            } else {
                Toast.makeText(this, "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
