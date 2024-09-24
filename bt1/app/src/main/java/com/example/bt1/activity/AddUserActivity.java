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

public class AddUserActivity extends AppCompatActivity {

    private EditText txtUsername, txtPassword, txtFullname, txtEmail;
    private Button btnAddUser;
    private Spinner spinnerGender;
    private DatePicker datePickerDob;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtFullname = findViewById(R.id.txtFullname);
        txtEmail = findViewById(R.id.txtEmail);
        spinnerGender = findViewById(R.id.spinnerGender);
        datePickerDob = findViewById(R.id.datePickerDob);
        btnAddUser = findViewById(R.id.btnAddUser);

        // Set up gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Initialize UserDAO
        userDAO = new UserDAO(this);

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle adding the user
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                String fullname = txtFullname.getText().toString();
                String email = txtEmail.getText().toString();
                String gender = spinnerGender.getSelectedItem().toString();

                // Get the date from DatePicker
                int day = datePickerDob.getDayOfMonth();
                int month = datePickerDob.getMonth();
                int year = datePickerDob.getYear();

                // Create a Calendar object and set the date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                // Convert the Calendar date to a Date object
                Date dob = calendar.getTime();

                // Check if the username or email already exists
                if (userDAO.findUserByUsername(username) != null) {
                    Toast.makeText(AddUserActivity.this, "Username was used. Please try another username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (userDAO.findAllUsersByEmail(email).size() > 0) {
                    Toast.makeText(AddUserActivity.this, "Email was used. Please try another email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(username, password, fullname, email, gender, dob);
                long userId = userDAO.createUser(user);

                if (userId > 0) {
                    Toast.makeText(AddUserActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddUserActivity.this, "Failed to add user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
