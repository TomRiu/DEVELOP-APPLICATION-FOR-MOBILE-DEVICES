package com.example.bt1.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bt1.R;

public class UserHomeActivity extends AppCompatActivity {

    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhome);

        tvWelcome = findViewById(R.id.tvWelcome);

        // Get the username from the intent
        String username = getIntent().getStringExtra("username");

        // Set the welcome message
        tvWelcome.setText("Hello, " + username + "!");
    }
}
