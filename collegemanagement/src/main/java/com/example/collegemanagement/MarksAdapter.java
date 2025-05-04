package com.example.collegemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MarksViewHolder> {

    Context context;
    ArrayList<Module> modules;
    boolean isEditable; // true = Teacher view, false = Student view

    public MarksAdapter(Context context, ArrayList<Module> modules, boolean isEditable) {
        this.context = context;
        this.modules = modules;
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public MarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isEditable) {
            // Inflate the editable item layout (for Teacher)
            view = LayoutInflater.from(context).inflate(R.layout.item_mark_teacher, parent, false);
        } else {
            // Inflate the read-only item layout (for Student)
            view = LayoutInflater.from(context).inflate(R.layout.item_mark_student, parent, false);
        }
        return new MarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksViewHolder holder, int position) {
        Module module = modules.get(position);

        holder.moduleName.setText(module.getName());

        if (isEditable) {
            // If editable, set text for EditTexts
            if (module.getTdMark() != -1) holder.tdMark.setText(String.valueOf(module.getTdMark()));
            else holder.tdMark.setText("");

            if (module.getTpMark() != -1) holder.tpMark.setText(String.valueOf(module.getTpMark()));
            else holder.tpMark.setText("");

            if (module.getExamMark() != -1) holder.examMark.setText(String.valueOf(module.getExamMark()));
            else holder.examMark.setText("");
        } else {
            // If not editable, show marks in TextViews
            holder.tdText.setText("TD: " + (module.getTdMark() != -1 ? module.getTdMark() : "N/A"));
            holder.tpText.setText("TP: " + (module.getTpMark() != -1 ? module.getTpMark() : "N/A"));
            holder.examText.setText("Exam: " + (module.getExamMark() != -1 ? module.getExamMark() : "N/A"));
        }
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public class MarksViewHolder extends RecyclerView.ViewHolder {
        TextView moduleName;
        EditText tdMark, tpMark, examMark;
        TextView tdText, tpText, examText;

        public MarksViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.moduleName);

            if (isEditable) {
                tdMark = itemView.findViewById(R.id.tdMark);
                tpMark = itemView.findViewById(R.id.tpMark);
                examMark = itemView.findViewById(R.id.examMark);
            } else {
                tdText = itemView.findViewById(R.id.tdMarkText);
                tpText = itemView.findViewById(R.id.tpMarkText);
                examText = itemView.findViewById(R.id.examMarkText);
            }
        }
    }
}
