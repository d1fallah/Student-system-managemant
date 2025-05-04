package com.example.collegemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT = 1000; // 1 second splash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            String userRole = sharedPreferences.getString("userRole", "");

            Intent intent;
            if (isLoggedIn) {
                // If already logged in
                if (userRole.equalsIgnoreCase("admin")) {
                    intent = new Intent(MainActivity.this, AdminPage.class);
                } else if (userRole.equalsIgnoreCase("teacher")) {
                    intent = new Intent(MainActivity.this, TeacherPage.class);
                } else if (userRole.equalsIgnoreCase("student")) {
                    intent = new Intent(MainActivity.this, StudentPage.class);
                } else {
                    // Unknown role fallback
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
            } else {
                // Not logged in
                intent = new Intent(MainActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish(); // close splash
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
