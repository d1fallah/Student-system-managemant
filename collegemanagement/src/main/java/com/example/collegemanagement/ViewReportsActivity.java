package com.example.collegemanagement;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewReportsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ReportsAdapter adapter;
    ArrayList<Report> reports = new ArrayList<>();
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports); // 🔥 We'll create it next

        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadReports();
    }

    private void loadReports() {
        reports.clear();
        Cursor cursor = dbHelper.getAllReports();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String userRole = cursor.getString(cursor.getColumnIndexOrThrow("user_role"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

                reports.add(new Report(id, userId, userRole, title, description, status));
            } while (cursor.moveToNext());

            cursor.close();
        }

        adapter = new ReportsAdapter(reports, this::showStatusDialog);
        recyclerView.setAdapter(adapter);
    }

    private void showStatusDialog(Report report) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Report Status");
        builder.setItems(new String[]{"Pending", "Resolved"}, (dialog, which) -> {
            String newStatus = (which == 0) ? "Pending" : "Resolved";

            // 🔥 Confirmation dialog after choosing
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to set status as \"" + newStatus + "\"?")
                    .setPositiveButton("Yes", (confirmDialog, w) -> {
                        boolean updated = dbHelper.updateReportStatus(report.getId(), newStatus);

                        if (updated) {
                            Toast.makeText(this, "Status updated!", Toast.LENGTH_SHORT).show();
                            loadReports(); // Refresh
                        } else {
                            Toast.makeText(this, "Failed to update status!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (confirmDialog, w) -> {
                        dialog.dismiss(); // 🔥 Dismiss original dialog if cancel
                        adapter.notifyDataSetChanged(); // 🔥 Refresh item to undo swipe/highlight
                    })
                    .show();
        });
        builder.show();
    }

}
