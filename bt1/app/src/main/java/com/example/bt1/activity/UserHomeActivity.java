package com.example.bt1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.R;
import com.example.bt1.model.User;

public class UserHomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnAddUser, btnSearchUser, btnEditUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhome);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnAddUser = findViewById(R.id.btnAddUser);
        btnSearchUser = findViewById(R.id.btnSearchUser);
        btnEditUser = findViewById(R.id.btnEditUser);

        User user = (User) getIntent().getSerializableExtra("user");

        tvWelcome.setText("Hello, " + user.getUsername() + "!");

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, SearchUserActivity.class);
                startActivity(intent);
            }
        });

        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, EditUserActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
}
