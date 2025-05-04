package com.example.collegemanagement;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentMarksActivity extends AppCompatActivity {

    private TextView titleTextView, resultTextView;
    private RecyclerView recyclerView;
    private Button calculateButton;

    private MarksAdapter marksAdapter;
    private ArrayList<Module> modules = new ArrayList<>();

    private DBHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marks);

        // Initialize views
        titleTextView = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recyclerView);
        calculateButton = findViewById(R.id.calculateBtn);
        resultTextView = findViewById(R.id.resultText);

        dbHelper = new DBHelper(this);

        // Get student ID from shared preferences
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        studentId = prefs.getInt("studentId", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // RecyclerView setup
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        marksAdapter = new MarksAdapter(this, modules, false); // false = student (read-only)
        recyclerView.setAdapter(marksAdapter);

        loadStudentMarks();

        calculateButton.setOnClickListener(v -> calculateAverage());
    }

    private void loadStudentMarks() {
        modules.clear();
        Cursor cursor = dbHelper.getMarksForStudent(studentId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String moduleName = cursor.getString(cursor.getColumnIndexOrThrow("module_name"));
                double tdMark = cursor.getDouble(cursor.getColumnIndexOrThrow("td_mark"));
                double tpMark = cursor.getDouble(cursor.getColumnIndexOrThrow("tp_mark"));
                double examMark = cursor.getDouble(cursor.getColumnIndexOrThrow("exam_mark"));
                int coefficient = cursor.getInt(cursor.getColumnIndexOrThrow("coefficient"));

                Module module = new Module(moduleName, coefficient, true, true);
                module.setTdMark(tdMark);
                module.setTpMark(tpMark);
                module.setExamMark(examMark);

                modules.add(module);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No marks found!", Toast.LENGTH_SHORT).show();
        }

        marksAdapter.notifyDataSetChanged();
    }

    private void calculateAverage() {
        double totalWeightedMarks = 0;
        int totalCoefficients = 0;

        for (Module module : modules) {
            double td = module.getTdMark();
            double tp = module.getTpMark();
            double exam = module.getExamMark();

            // TD + TP contribute 40%, Exam contributes 60%
            double courseworkAverage = (td + tp) / 2.0;
            double moduleAverage = (courseworkAverage * 0.4) + (exam * 0.6);

            totalWeightedMarks += moduleAverage * module.getCoefficient();
            totalCoefficients += module.getCoefficient();
        }

        double finalAverage = (totalCoefficients == 0) ? 0 : totalWeightedMarks / totalCoefficients;

        // Display the calculated average
        resultTextView.setText(String.format("Average: %.2f", finalAverage));

        // Show pass/fail message
        if (finalAverage >= 10.0) {
            Toast.makeText(this, "🎉 Congratulations, you passed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "❌ Unfortunately, you failed.", Toast.LENGTH_LONG).show();
        }
    }

}
