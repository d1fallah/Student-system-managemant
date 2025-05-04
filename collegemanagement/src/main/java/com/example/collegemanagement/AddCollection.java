package com.example.collegemanagement;

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class AddCollection extends AppCompatActivity {

    String collection;
    DBHelper dbHelper;
    LinkedList<Map.Entry<String, EditText>> editTexts = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        dbHelper = new DBHelper(this);

        collection = getIntent().getStringExtra("collection");

        String heading = "Add " + collection.substring(0, 1).toUpperCase() + collection.substring(1);
        ((TextView) findViewById(R.id.addCollectionHead)).setText(heading);

        HashMap<String, Integer> drawables = new HashMap<>();
        drawables.put("student", R.drawable.student);
        drawables.put("teacher", R.drawable.teacher);

        ((ImageView) findViewById(R.id.viewDocImage)).setImageResource(drawables.get(collection));

        LinearLayout addLayout = findViewById(R.id.addColLayout);

        // Predefined fields
        String[] fields;
        if (collection.equals("student")) {
            fields = new String[]{"Name", "Address", "DOB", "Course", "Roll Number", "Email", "Password"};
        } else {
            fields = new String[]{"Name", "Address", "DOB", "Salary", "Email", "Password"};
        }

        // Create UI dynamically
        for (String field : fields) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(20f);
            textView.setText(field);
            addLayout.addView(textView);

            EditText editText = new EditText(this);
            editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            editText.setTextSize(20f);
            if (field.equalsIgnoreCase("password")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            editTexts.add(new AbstractMap.SimpleEntry<>(field, editText));
            addLayout.addView(editText);
        }

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(view -> {
            Map<String, String> data = new HashMap<>();
            for (Map.Entry<String, EditText> entry : editTexts) {
                data.put(entry.getKey(), entry.getValue().getEditableText().toString().trim());
            }

            boolean success;
            if (collection.equals("student")) {
                success = dbHelper.insertStudent(
                        data.get("Name"),
                        data.get("Address"),
                        data.get("DOB"),
                        data.get("Course"),
                        data.get("Roll Number"),
                        data.get("Email"),
                        data.get("Password")
                );
            } else { // teacher
                success = dbHelper.insertTeacher(
                        data.get("Name"),
                        data.get("Address"),
                        data.get("DOB"),
                        data.get("Salary"),
                        data.get("Email"),
                        data.get("Password")
                );
            }

            if (success) {
                Toast.makeText(getApplicationContext(), "Added to " + collection, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to add to " + collection, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
