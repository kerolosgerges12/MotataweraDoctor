package com.kmk.motatawera.doctor.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.SubjectLayoutBinding;
import com.kmk.motatawera.doctor.model.SubjectModel;
import com.kmk.motatawera.doctor.ui.AddQuizActivity;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    String branch;
    private Context context;
    private List<SubjectModel> subjectsModelList;

    public SubjectAdapter(Context context, List<SubjectModel> subjectsModelList) {
        this.context = context;
        this.subjectsModelList = subjectsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubjectLayoutBinding subjectLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.subject_layout, parent, false);
        return new ViewHolder(subjectLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubjectModel subjectsModel = subjectsModelList.get(position);

        if (subjectsModel.getBranch() == 1)
            branch = "Haram";
        else
            branch = "Qtamia";


        holder.subjectLayoutBinding.txtSubjectLayoutName.setText(subjectsModel.getName());
        holder.subjectLayoutBinding.txtSubjectLayoutBranch.setText(branch);
        holder.subjectLayoutBinding.SubjectLayout.setOnClickListener(v -> {

            Intent intent = new Intent(context, AddQuizActivity.class);
            intent.putExtra("subject_id", subjectsModel.getId())
                    .putExtra("subject_branch", subjectsModel.getBranch())
                    .putExtra("subject_department", subjectsModel.getDepartment())
                    .putExtra("subject_grad", subjectsModel.getGrad())
                    .putExtra("subject_name", subjectsModel.getName());
                    context.startActivity(intent);
            ((Activity)context).finish();
        });


    }

    public void setList(List<SubjectModel> subjectsModelList) {
        this.subjectsModelList = subjectsModelList;
        getItemCount();
        notifyItemInserted(subjectsModelList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return subjectsModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        SubjectLayoutBinding subjectLayoutBinding;

        public ViewHolder(@NonNull SubjectLayoutBinding subjectLayoutBinding) {
            super(subjectLayoutBinding.getRoot());
            this.subjectLayoutBinding = subjectLayoutBinding;

        }


    }


}
