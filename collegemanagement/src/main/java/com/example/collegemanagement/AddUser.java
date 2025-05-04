package com.example.collegemanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddUser extends AppCompatActivity {

    private EditText dateOfBirth;
    Button submitBtn;
    EditText firstName, lastName, address, salary, mail, passwordField;
    String fName, lName, dob, add, sal, email, password, selectedRole;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        dbHelper = new DBHelper(this);

        // Input fields
        firstName = findViewById(R.id.fName);
        lastName = findViewById(R.id.lName);
        address = findViewById(R.id.address);
        dateOfBirth = findViewById(R.id.dob);
        salary = findViewById(R.id.salary);
        mail = findViewById(R.id.mail);
        passwordField = findViewById(R.id.password);
        submitBtn = findViewById(R.id.submitBtn);

        // DOB picker
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateOfBirth.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), (datePicker, year1, month1, day1) -> {
                month1 = month1 + 1;
                String date = day1 + "-" + month1 + "-" + year1;
                dateOfBirth.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        // Get selected role from Intent (passed by AdminPage)
        selectedRole = getIntent().getStringExtra("role");
        if (selectedRole == null) {
            selectedRole = "student"; // default to student if not provided
        }

        // Submit button click
        submitBtn.setOnClickListener(view -> {
            fName = firstName.getText().toString().trim();
            lName = lastName.getText().toString().trim();
            add = address.getText().toString().trim();
            dob = dateOfBirth.getText().toString().trim();
            sal = salary.getText().toString().trim(); // Only used for teachers
            email = mail.getText().toString().trim();
            password = passwordField.getText().toString().trim();

            String fullName = fName + " " + lName;

            if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert into users table
            boolean userInserted = dbHelper.insertUser(fullName, email, password, selectedRole);
            boolean roleInserted = false;

            if (selectedRole.equalsIgnoreCase("student")) {
                roleInserted = dbHelper.insertStudent(fullName, add, dob, "N/A", "N/A", email, password);
            } else if (selectedRole.equalsIgnoreCase("teacher")) {
                roleInserted = dbHelper.insertTeacher(fullName, add, dob, sal, email, password);
            }

            if (userInserted && roleInserted) {
                Toast.makeText(getApplicationContext(), capitalizeFirstLetter(selectedRole) + " added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Error adding " + selectedRole, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
