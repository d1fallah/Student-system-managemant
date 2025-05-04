package com.example.collegemanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewDocuments extends AppCompatActivity {

    RecyclerDocumentAdapter adapter;
    ArrayList<Map.Entry<String, String>> list = new ArrayList<>();
    String collection, userType;
    Button addBtn;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_documents);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        userType = intent.getStringExtra("userType");
        collection = intent.getStringExtra("collection");

        // Set page heading
        String heading = collection.substring(0, 1).toUpperCase() + collection.substring(1) + " List";
        ((TextView) findViewById(R.id.viewCollection)).setText(heading);

        // Image settings
        HashMap<String, Integer> drawables = new HashMap<>();
        drawables.put("student", R.drawable.student);
        drawables.put("teacher", R.drawable.teacher);

        Integer imageRes = drawables.get(collection);
        if (imageRes != null) {
            ((ImageView) findViewById(R.id.viewDocImage)).setImageResource(imageRes);
        } else {
            ((ImageView) findViewById(R.id.viewDocImage)).setImageResource(R.drawable.default_image);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addBtn = findViewById(R.id.button);

        if (collection.equals("teacher") || collection.equals("student")) {
            addBtn.setOnClickListener(v -> {
                Intent i = new Intent(getApplicationContext(), AddUser.class);
                i.putExtra("role", collection);
                startActivity(i);
            });
        } else {
            addBtn.setEnabled(false);
        }

        loadData();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadData() {
        list.clear();
        Cursor cursor = null;

        if (collection.equals("teacher")) {
            cursor = dbHelper.getAllTeachers();
        } else if (collection.equals("student")) {
            cursor = dbHelper.getAllStudents();
        } else if (collection.equals("marks")) {
            fetchModulesFromServer();
            return; // important to stop here
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                StringBuilder item = new StringBuilder();
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    item.append(cursor.getColumnName(i)).append(": ").append(cursor.getString(i)).append("\n");
                }
                list.add(new AbstractMap.SimpleEntry<>(String.valueOf(id), item.toString()));
            } while (cursor.moveToNext());
            cursor.close();
        } else if (!collection.equals("marks")) {
            Toast.makeText(this, "No data available for " + collection, Toast.LENGTH_SHORT).show();
        }

        adapter = new RecyclerDocumentAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void fetchModulesFromServer() {
        String url = "https://num.univ-biskra.dz/psp/formations/get_modules_json?sem=1&spec=184";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    list.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String moduleName = obj.getString("Nom_module");
                            double coefficient = obj.getDouble("Coefficient");
                            boolean hasTD = obj.optInt("td", 0) == 1;
                            boolean hasTP = obj.optInt("tp", 0) == 1;

                            String details = "Coefficient: " + coefficient +
                                    "\nTD: " + (hasTD ? "Yes" : "No") +
                                    "\nTP: " + (hasTP ? "Yes" : "No");

                            list.add(new AbstractMap.SimpleEntry<>(String.valueOf(i), moduleName + "\n" + details));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter = new RecyclerDocumentAdapter(this, list);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Failed to load modules", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(jsonArrayRequest);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            String id = adapter.getAdapterId(pos);

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    new AlertDialog.Builder(viewHolder.itemView.getContext())
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                boolean success = false;
                                if (collection.equals("teacher")) {
                                    success = dbHelper.deleteTeacher(Integer.parseInt(id));
                                } else if (collection.equals("student")) {
                                    success = dbHelper.deleteStudent(Integer.parseInt(id));
                                }

                                if (success) {
                                    Toast.makeText(ViewDocuments.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ViewDocuments.this, "Delete failed", Toast.LENGTH_SHORT).show();
                                }
                                loadData(); // Reload data
                            })
                            .setNegativeButton("No", (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                            .create()
                            .show();
                    break;

                case ItemTouchHelper.RIGHT:
                    Toast.makeText(ViewDocuments.this, "Update not implemented yet", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    break;
            }
        }
    };
}
