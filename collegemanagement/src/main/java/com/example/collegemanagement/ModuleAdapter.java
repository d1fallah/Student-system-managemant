package com.example.collegemanagement;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private List<Module> modules;

    public ModuleAdapter(List<Module> modules) {
        this.modules = modules;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = modules.get(position);

        holder.moduleName.setText(module.getName());
        holder.moduleCoeff.setText("Coeff: " + module.getCoefficient());

        // Hide TD or TP if not needed
        if (!module.hasTD()) {
            holder.tdMark.setVisibility(View.GONE);
        } else {
            holder.tdMark.setVisibility(View.VISIBLE);
        }

        if (!module.hasTP()) {
            holder.tpMark.setVisibility(View.GONE);
        } else {
            holder.tpMark.setVisibility(View.VISIBLE);
        }

        // Listen for mark changes
        holder.examMark.addTextChangedListener(new MarkWatcher(holder, module, "exam"));
        holder.tdMark.addTextChangedListener(new MarkWatcher(holder, module, "td"));
        holder.tpMark.addTextChangedListener(new MarkWatcher(holder, module, "tp"));
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView moduleName, moduleCoeff;
        EditText tdMark, tpMark, examMark;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.moduleName);
            moduleCoeff = itemView.findViewById(R.id.moduleCoeff);
            tdMark = itemView.findViewById(R.id.tdMark);
            tpMark = itemView.findViewById(R.id.tpMark);
            examMark = itemView.findViewById(R.id.examMark);
        }
    }

    private static class MarkWatcher implements TextWatcher {
        private ModuleViewHolder holder;
        private Module module;
        private String fieldType;

        public MarkWatcher(ModuleViewHolder holder, Module module, String fieldType) {
            this.holder = holder;
            this.module = module;
            this.fieldType = fieldType;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
        @Override
        public void afterTextChanged(Editable s) {
            try {
                double mark = Double.parseDouble(s.toString());
                if (fieldType.equals("td")) module.setTdMark(mark);
                if (fieldType.equals("tp")) module.setTpMark(mark);
                if (fieldType.equals("exam")) module.setExamMark(mark);
            } catch (NumberFormatException e) {
                // Ignore empty input
            }
        }
    }
}
