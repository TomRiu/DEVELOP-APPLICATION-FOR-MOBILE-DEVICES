package com.example.bt1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.R;
import com.example.bt1.model.User;
import com.example.bt1.dao.UserDAO;

public class EditUserActivity extends AppCompatActivity {

    private EditText txtEditUsername, txtEditPassword, txtEditFullname, txtEditEmail;
    private Button btnReset, btnSave;
    private UserDAO userDAO;

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

        // Initialize UserDAO
        userDAO = new UserDAO(this);

        User user = (User) getIntent().getSerializableExtra("user");

        assert user != null;
        txtEditUsername.setText(user.getUsername());
        txtEditPassword.setText(user.getPassword());
        txtEditFullname.setText(user.getFullname());
        txtEditEmail.setText(user.getEmail());

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEditUsername.setText(user.getUsername());
                txtEditPassword.setText(user.getPassword());
                txtEditFullname.setText(user.getFullname());
                txtEditEmail.setText(user.getEmail());
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtEditUsername.getText().toString();
                String newPassword = txtEditPassword.getText().toString();
                String newFullname = txtEditFullname.getText().toString();
                String newEmail = txtEditEmail.getText().toString();

                // Check if the new email already exists
                if (userDAO.findAllUsersByEmail(newEmail).size() > 0 && !newEmail.equals(user.getEmail())) {
                    Toast.makeText(EditUserActivity.this, "Email was used. Please try another email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.setPassword(newPassword);
                user.setFullname(newFullname);
                user.setEmail(newEmail);

                int rowsUpdated = userDAO.updateUser(user);

                if (rowsUpdated > 0) {
                    Toast.makeText(EditUserActivity.this, "User information updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditUserActivity.this, "Failed to update user information!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
