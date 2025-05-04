package com.example.collegemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TeacherPage extends AppCompatActivity {

    TextView userName;
    CardView signOutBtn, marksBtn, attendanceBtn, subjectsBtn;

    DBHelper dbHelper;
    int teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_page);

        dbHelper = new DBHelper(this);

        userName = findViewById(R.id.userName);
        signOutBtn = findViewById(R.id.signOutBtn);
        attendanceBtn = findViewById(R.id.attendanceBtn);
        subjectsBtn = findViewById(R.id.subjectsBtn);
        marksBtn = findViewById(R.id.marksBtn);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        teacherId = prefs.getInt("teacherId", -1); // ✅ Correct key

        if (teacherId != -1) {
            loadTeacherName(teacherId);
        } else {
            Toast.makeText(this, "Login expired. Please login again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TeacherPage.this, LoginActivity.class));
            finish();
        }


        // Marks ➔ View Students for Marks
        marksBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherPage.this, ViewStudentsForMarks.class);
            startActivity(intent);
        });

        // Attendance ➔ Send Report
        attendanceBtn.setOnClickListener(view -> {
            Intent i = new Intent(TeacherPage.this, SendReportActivity.class);
            startActivity(i);
        });

        // Subjects ➔ View Profile
        subjectsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherPage.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Sign out
        signOutBtn.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(TeacherPage.this, LoginActivity.class));
            finish();
        });
    }

    private void loadTeacherName(int id) {
        Cursor cursor = dbHelper.getTeacherById(id);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                userName.setText("Welcome, " + name);
            } else {
                Log.e("TeacherPage", "No teacher found with ID: " + id);
                userName.setText("Welcome, Teacher");
            }
            cursor.close();
        } else {
            Log.e("TeacherPage", "Cursor is null for teacher ID: " + id);
            userName.setText("Welcome, Teacher");
        }
    }

}
