package com.example.collegemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    Context context;
    ArrayList<Student> students;
    OnStudentClickListener listener;

    public StudentAdapter(Context context, ArrayList<Student> students, OnStudentClickListener listener) {
        this.context = context;
        this.students = students;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.name.setText(student.getName());
        holder.cardView.setOnClickListener(v -> listener.onStudentClick(student));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentName);
            cardView = itemView.findViewById(R.id.studentCard);
        }
    }
}
