package com.example.bt1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.R;
import com.example.bt1.model.User;
import com.example.bt1.dao.UserDAO;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Initialize UserDAO
        userDAO = new UserDAO(this);

        if (userDAO.findUserByUsername("admin") == null) {
            User admin = new User("admin", "admin", "Admin User", "admin@email.com", "Male", new Date(23042003));
            userDAO.createUser(admin);
        }


        ClickOnLogin();
    }

    private void ClickOnLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                User currentUser = userDAO.findUserByUsername(username);

                if(currentUser == null) Toast.makeText(LoginActivity.this, "Login failed. Invalid username. Please try again.", Toast.LENGTH_SHORT).show();
                else if (currentUser.getPassword().equals(password)) {
                    // Login successful
                    Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                    intent.putExtra("user", currentUser);
                    startActivity(intent);
                    finish(); // Finish the LoginActivity so the user can't go back to it
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Login failed. Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
