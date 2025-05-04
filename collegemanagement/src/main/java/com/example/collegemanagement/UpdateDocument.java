package com.example.collegemanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UpdateDocument extends AppCompatActivity {

    DBHelper dbHelper;
    String collection;
    String docId;
    LinkedList<Map.Entry<String, EditText>> editTexts = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_document);

        dbHelper = new DBHelper(this);

        Intent update = getIntent();
        collection = update.getStringExtra("collection");
        docId = update.getStringExtra("docId"); // This is actually the ID (row ID) in SQLite

        String heading = "Update " + collection.substring(0, 1).toUpperCase() + collection.substring(1);
        ((TextView) findViewById(R.id.viewDocHead)).setText(heading);

        HashMap<String, Integer> drawables = new HashMap<>();
        drawables.put("student", R.drawable.student);
        drawables.put("teacher", R.drawable.teacher);

        ((ImageView) findViewById(R.id.updateDocImage)).setImageResource(drawables.get(collection));

        LinearLayout updateDocList = findViewById(R.id.updateDocList);

        // Load data from SQLite
        Cursor cursor;
        if (collection.equals("teacher")) {
            cursor = dbHelper.getTeacherById(Integer.parseInt(docId));
        } else {
            cursor = dbHelper.getStudentById(Integer.parseInt(docId));
        }

        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 1; i < cursor.getColumnCount(); i++) {
                String columnName = cursor.getColumnName(i);
                String columnValue = cursor.getString(i);

                TextView textView = new TextView(this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(20f);
                textView.setText(columnName);
                updateDocList.addView(textView);

                EditText editText = new EditText(this);
                editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setTextSize(20f);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setText(columnValue);
                editTexts.add(new AbstractMap.SimpleEntry<>(columnName, editText));
                updateDocList.addView(editText);
            }
            cursor.close();
        }

        Button updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            for (Map.Entry<String, EditText> entry : editTexts) {
                values.put(entry.getKey(), entry.getValue().getText().toString().trim());
            }

            boolean success;
            if (collection.equals("teacher")) {
                success = dbHelper.updateTeacher(Integer.parseInt(docId), values);
            } else {
                success = dbHelper.updateStudent(Integer.parseInt(docId), values);
            }

            if (success) {
                Toast.makeText(UpdateDocument.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(UpdateDocument.this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
