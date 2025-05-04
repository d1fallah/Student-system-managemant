package com.example.collegemanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyProfileActivity extends AppCompatActivity {

    EditText inputUserEmail, nameInput, emailInput, passwordInput;
    Spinner userTypeSpinner;
    Button searchBtn, updateButton;
    DBHelper dbHelper;
    String selectedUserType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);

        // Initialize Views
        inputUserEmail = findViewById(R.id.inputUserEmail);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        searchBtn = findViewById(R.id.searchBtn);
        updateButton = findViewById(R.id.updateButton);

        dbHelper = new DBHelper(this);

        // Setup Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"student", "teacher"});
        userTypeSpinner.setAdapter(adapter);

        userTypeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedUserType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // Search User Button
        searchBtn.setOnClickListener(v -> {
            String email = inputUserEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor;
            if (selectedUserType.equalsIgnoreCase("student")) {
                cursor = dbHelper.getStudentByEmail(email);
            } else {
                cursor = dbHelper.getTeacherByEmail(email);
            }

            if (cursor != null && cursor.moveToFirst()) {
                nameInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                emailInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                passwordInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                cursor.close();
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Update Profile Button
        updateButton.setOnClickListener(v -> {
            String email = inputUserEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Search for a user first!", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put("name", nameInput.getText().toString().trim());
            values.put("email", emailInput.getText().toString().trim());
            values.put("password", passwordInput.getText().toString().trim());

            boolean updated = false;

            if (selectedUserType.equalsIgnoreCase("student")) {
                Cursor cursor = dbHelper.getStudentByEmail(email);
                if (cursor != null && cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    updated = dbHelper.updateStudent(id, values);
                    cursor.close();
                }
            } else if (selectedUserType.equalsIgnoreCase("teacher")) {
                Cursor cursor = dbHelper.getTeacherByEmail(email);
                if (cursor != null && cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    updated = dbHelper.updateTeacher(id, values);
                    cursor.close();
                }
            }

            if (updated) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
