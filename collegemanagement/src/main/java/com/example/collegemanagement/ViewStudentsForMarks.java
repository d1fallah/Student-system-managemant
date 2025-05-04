package com.example.collegemanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewStudentsForMarks extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper dbHelper;
    ArrayList<Student> studentList = new ArrayList<>();
    StudentAdapter adapter; // We will make this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_for_marks);

        recyclerView = findViewById(R.id.recyclerView);
        dbHelper = new DBHelper(this);

        loadStudents();
    }

    private void loadStudents() {
        Cursor cursor = dbHelper.getAllStudents();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                studentList.add(new Student(id, name));
            } while (cursor.moveToNext());
            cursor.close();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, studentList, student -> {
            Intent intent = new Intent(ViewStudentsForMarks.this, EnterMarksActivity.class);
            intent.putExtra("studentId", student.getId());  // 🛑 pass correct student ID!
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
