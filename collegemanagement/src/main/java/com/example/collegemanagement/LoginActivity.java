package com.example.collegemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button login;
    ProgressBar circularPB;
    EditText inputEmail, inputPass;
    TextView forgotPass;
    DBHelper dbHelper;

    final String emailRegEx = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    final String passwordRegEx = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        inputEmail = findViewById(R.id.emailId);
        inputPass = findViewById(R.id.password);
        forgotPass = findViewById(R.id.forgot_pwd);
        login = findViewById(R.id.login_btn);
        circularPB = findViewById(R.id.progressBar);

        forgotPass.setOnClickListener(v ->
                Toast.makeText(this, "Forgot password not supported offline", Toast.LENGTH_SHORT).show()
        );

        login.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPass.getText().toString().trim();

            boolean emailValidated = validation(email, emailRegEx);
            boolean passwordValidated = validation(password, passwordRegEx);

            if (emailValidated && passwordValidated) {
                circularPB.setVisibility(View.VISIBLE);
                loginUser(email, password);
            } else if (!emailValidated) {
                Toast.makeText(this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validation(String string, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        return pattern.matcher(string).matches();
    }

    private void loginUser(String email, String password) {
        // Check user table first (admin)
        Cursor cursor = dbHelper.checkUserCredentials(email, password);

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_ROLE));

            if (role.equalsIgnoreCase("admin")) {
                SharedPreferences.Editor editor = getSharedPreferences("user_session", MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("userRole", "admin");
                editor.apply();

                cursor.close(); // ✅ close before move
                startActivity(new Intent(LoginActivity.this, AdminPage.class));
                finish();
                return;
            }
            cursor.close();
        }

        // Check student
        Cursor studentCursor = dbHelper.checkStudentCredentials(email, password);
        if (studentCursor != null && studentCursor.moveToFirst()) {
            int studentId = studentCursor.getInt(studentCursor.getColumnIndexOrThrow("id"));
            String studentName = studentCursor.getString(studentCursor.getColumnIndexOrThrow("name"));
            String studentEmail = studentCursor.getString(studentCursor.getColumnIndexOrThrow("email")); // ✨

            SharedPreferences.Editor editor = getSharedPreferences("user_session", MODE_PRIVATE).edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("userRole", "student");
            editor.putInt("studentId", studentId);
            editor.putString("userName", studentName);
            editor.putString("userEmail", studentEmail); // ✨ save email too
            editor.apply();

            studentCursor.close();
            startActivity(new Intent(LoginActivity.this, StudentPage.class));
            finish();
            return;
        }


        // Check teacher
        Cursor teacherCursor = dbHelper.checkTeacherCredentials(email, password);
        if (teacherCursor != null && teacherCursor.moveToFirst()) {
            int teacherId = teacherCursor.getInt(teacherCursor.getColumnIndexOrThrow("id"));
            String teacherName = teacherCursor.getString(teacherCursor.getColumnIndexOrThrow("name"));
            String teacherEmail = teacherCursor.getString(teacherCursor.getColumnIndexOrThrow("email")); // ✨

            SharedPreferences.Editor editor = getSharedPreferences("user_session", MODE_PRIVATE).edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("userRole", "teacher");
            editor.putInt("teacherId", teacherId);
            editor.putString("userName", teacherName);
            editor.putString("userEmail", teacherEmail); // ✨ save email too
            editor.apply();

            teacherCursor.close();
            startActivity(new Intent(LoginActivity.this, TeacherPage.class));
            finish();
            return;
        }


        Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
    }



}
