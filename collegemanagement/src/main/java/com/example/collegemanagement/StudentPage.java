package com.example.collegemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class StudentPage extends AppCompatActivity {

    TextView userName;
    CardView signOutBtn, marksBtn, attendanceBtn, subjectsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);

        userName = findViewById(R.id.userName);
        signOutBtn = findViewById(R.id.signOutBtn);
        marksBtn = findViewById(R.id.marksBtn);
        attendanceBtn = findViewById(R.id.attendanceBtn);
        subjectsBtn = findViewById(R.id.subjectsBtn);

        // Get username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String username = prefs.getString("userName", "Student");
        userName.setText("Welcome, " + username);

        // Marks ➔ View Student Marks
        marksBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StudentPage.this, StudentMarksActivity.class);
            startActivity(intent);

        });

        // Attendance ➔ Send Report
        attendanceBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StudentPage.this, SendReportActivity.class);
            startActivity(intent);
        });

        // Subjects ➔ View Profile
        subjectsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StudentPage.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Logout
        signOutBtn.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StudentPage.this, LoginActivity.class));
            finish();
        });
    }
}
