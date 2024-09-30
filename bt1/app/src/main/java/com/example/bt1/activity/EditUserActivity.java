package com.example.bt1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.R;
import com.example.bt1.model.User;
import com.example.bt1.dao.UserDAO;

import java.util.Calendar;
import java.util.Date;

public class EditUserActivity extends AppCompatActivity {

    private EditText txtEditUsername, txtEditPassword, txtEditFullname, txtEditEmail;
    private Button btnReset, btnSave;
    private Spinner spinnerEditGender;
    private DatePicker datePickerEditDob;
    private UserDAO userDAO;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user);

        txtEditUsername = findViewById(R.id.txtEditUsername);
        txtEditPassword = findViewById(R.id.txtEditPassword);
        txtEditFullname = findViewById(R.id.txtEditFullname);
        txtEditEmail = findViewById(R.id.txtEditEmail);
        btnReset = findViewById(R.id.btnReset);
        btnSave = findViewById(R.id.btnSave);
        spinnerEditGender = findViewById(R.id.spinnerEditGender);
        datePickerEditDob = findViewById(R.id.datePickerEditDob);

        // Initialize UserDAO
        userDAO = new UserDAO(this);

        // Setup gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditGender.setAdapter(adapter);

        user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            txtEditUsername.setText(user.getUsername());
            txtEditPassword.setText(user.getPassword());
            txtEditFullname.setText(user.getFullname());
            txtEditEmail.setText(user.getEmail());
            spinnerEditGender.setSelection(adapter.getPosition(user.getGender()));

            // Set DatePicker to user's date of birth
            Calendar cal = Calendar.getInstance();
            cal.setTime(user.getDob());
            datePickerEditDob.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserChanges();
            }
        });
    }

    private void resetFields() {
        if (user != null) {
            txtEditUsername.setText(user.getUsername());
            txtEditPassword.setText(user.getPassword());
            txtEditFullname.setText(user.getFullname());
            txtEditEmail.setText(user.getEmail());
            spinnerEditGender.setSelection(((ArrayAdapter<String>)spinnerEditGender.getAdapter()).getPosition(user.getGender()));

            Calendar cal = Calendar.getInstance();
            cal.setTime(user.getDob());
            datePickerEditDob.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
    }

    private void saveUserChanges() {
        String username = txtEditUsername.getText().toString();
        String newPassword = txtEditPassword.getText().toString();
        String newFullname = txtEditFullname.getText().toString();
        String newEmail = txtEditEmail.getText().toString();
        String newGender = spinnerEditGender.getSelectedItem().toString();

        // Get date from DatePicker
        int day = datePickerEditDob.getDayOfMonth();
        int month = datePickerEditDob.getMonth();
        int year = datePickerEditDob.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date newDob = calendar.getTime();

        // Check if the new email already exists
        if (userDAO.findAllUsersByEmail(newEmail).size() > 0 && !newEmail.equals(user.getEmail())) {
            Toast.makeText(this, "Email was used. Please try another email!", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setPassword(newPassword);
        user.setFullname(newFullname);
        user.setEmail(newEmail);
        user.setGender(newGender);
        user.setDob(newDob);

        int rowsUpdated = userDAO.updateUser(user);

        if (rowsUpdated > 0) {
            Toast.makeText(this, "User information updated successfully!", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity and return to the previous screen
        } else {
            Toast.makeText(this, "Failed to update user information!", Toast.LENGTH_SHORT).show();
        }
    }
}
