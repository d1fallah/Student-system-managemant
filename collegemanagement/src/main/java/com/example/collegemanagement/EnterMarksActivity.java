package com.example.collegemanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class EnterMarksActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button submitBtn;
    MarksAdapter marksAdapter;
    ArrayList<Module> modules = new ArrayList<>();
    DBHelper dbHelper;
    int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_marks);

        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        submitBtn = findViewById(R.id.submitBtn);

        studentId = getIntent().getIntExtra("studentId", -1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        marksAdapter = new MarksAdapter(this, modules, true);  // true = Teacher

        recyclerView.setAdapter(marksAdapter);

        fetchModules();  // 🔥 Fetch modules properly

        submitBtn.setOnClickListener(v -> saveMarks());
    }

    private void fetchModules() {
        modules.clear();
        String url = "https://num.univ-biskra.dz/psp/formations/get_modules_jsonsem?=1&spec=184";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject moduleObject = response.getJSONObject(i);

                            String moduleName = moduleObject.getString("Nom_module");
                            int coefficient = moduleObject.getInt("Coefficient");

                            // Detect if TD and TP exist
                            boolean hasTD = moduleObject.getString("td").equals("1");
                            boolean hasTP = moduleObject.getString("tp").equals("1");

                            modules.add(new Module(moduleName, coefficient, hasTD, hasTP));
                        }
                        marksAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing modules", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Failed to load modules", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonArrayRequest);
    }



    private void saveMarks() {
        boolean allSaved = true;

        for (Module module : modules) {
            boolean success = dbHelper.insertMark(
                    studentId,
                    module.getName(),
                    module.getTdMark(),
                    module.getTpMark(),
                    module.getExamMark(),
                    (int) module.getCoefficient()
            );
            if (!success) {
                allSaved = false;
            }
        }

        if (allSaved) {
            Toast.makeText(this, "All marks saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Some marks failed to save!", Toast.LENGTH_SHORT).show();
        }
    }
}
