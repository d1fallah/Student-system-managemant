package com.example.collegemanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminPage extends AppCompatActivity {

    CardView studentBtn, teacherBtn, modifyProfileBtn, marksBtn, sendReportsBtn, signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        // Find Views
        signOutBtn = findViewById(R.id.signOutBtn);
        studentBtn = findViewById(R.id.studentBtn);
        teacherBtn = findViewById(R.id.teacherBtn);
        modifyProfileBtn = findViewById(R.id.subjectsBtn); // 🔥 subjectsBtn is now Modify Profile
        sendReportsBtn = findViewById(R.id.attendanceBtn); // 🔥 attendanceBtn is now View Reports
        marksBtn = findViewById(R.id.marksBtn);

        // Buttons Listeners
        studentBtn.setOnClickListener(view -> openUserList("student"));
        teacherBtn.setOnClickListener(view -> openUserList("teacher"));

        modifyProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminPage.this, ModifyProfileActivity.class);
            startActivity(intent);
        });

        sendReportsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminPage.this, ViewReportsActivity.class);
            startActivity(intent);
        });

        marksBtn.setOnClickListener(view -> activityManager("marks"));

        signOutBtn.setOnClickListener(view -> showLogoutDialog());
    }

    private void openUserList(String role) {
        Intent intent = new Intent(AdminPage.this, ViewDocuments.class);
        intent.putExtra("collection", role);
        intent.putExtra("userType", "admin");
        startActivity(intent);
    }

    private void activityManager(String collection) {
        Intent intent = new Intent(AdminPage.this, ViewDocuments.class);
        intent.putExtra("collection", collection);
        intent.putExtra("userType", "admin");
        startActivity(intent);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    getSharedPreferences("user_session", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
}
