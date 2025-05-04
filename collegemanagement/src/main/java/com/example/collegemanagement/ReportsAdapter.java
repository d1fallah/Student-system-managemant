package com.example.collegemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {

    ArrayList<Report> reports;
    ReportClickListener listener;

    public interface ReportClickListener {
        void onReportClick(Report report);
    }

    public ReportsAdapter(ArrayList<Report> reports, ReportClickListener listener) {
        this.reports = reports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.title.setText(report.getTitle());
        holder.description.setText(report.getDescription());
        holder.status.setText("Status: " + report.getStatus());
        holder.userInfo.setText(report.getUserRole() + " ID: " + report.getUserId());

        holder.itemView.setOnClickListener(v -> listener.onReportClick(report));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, status, userInfo;
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.reportTitle);
            description = itemView.findViewById(R.id.reportDesc);
            status = itemView.findViewById(R.id.reportStatus);
            userInfo = itemView.findViewById(R.id.userInfo);
        }
    }
}
