package com.example.collegemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView nameTextView, emailTextView, roleTextView;
    Button backButton; // To go back to dashboard

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.profileName);
        emailTextView = findViewById(R.id.profileEmail);
        roleTextView = findViewById(R.id.profileRole);
        backButton = findViewById(R.id.backBtn);

        // 🔥 Load info from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String name = prefs.getString("userName", "N/A");
        String email = prefs.getString("userEmail", "N/A");
        String role = prefs.getString("userRole", "N/A");

        nameTextView.setText("Name: " + name);
        emailTextView.setText("Email: " + email);
        roleTextView.setText("Role: " + role);

        // Go back
        backButton.setOnClickListener(v -> finish());
    }
}
