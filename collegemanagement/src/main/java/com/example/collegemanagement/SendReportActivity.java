package com.example.collegemanagement;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SendReportActivity extends AppCompatActivity {

    EditText reportTitle, reportDescription;
    Button sendReportBtn;
    DBHelper dbHelper;
    String userRole;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        dbHelper = new DBHelper(this);

        reportTitle = findViewById(R.id.reportTitle);
        reportDescription = findViewById(R.id.reportDescription);
        sendReportBtn = findViewById(R.id.sendReportBtn);

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        userRole = prefs.getString("userRole", "");
        userId = (userRole.equals("student")) ? prefs.getInt("studentId", -1) : prefs.getInt("teacherId", -1);

        sendReportBtn.setOnClickListener(v -> sendReport());
    }

    private void sendReport() {
        String title = reportTitle.getText().toString().trim();
        String description = reportDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.insertReport(userId, userRole, title, description);
        if (success) {
            Toast.makeText(this, "Report sent successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to send report.", Toast.LENGTH_SHORT).show();
        }
    }

}
