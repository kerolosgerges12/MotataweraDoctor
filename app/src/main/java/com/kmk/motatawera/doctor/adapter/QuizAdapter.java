 package com.kmk.motatawera.doctor.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.QuizlistLayoutBinding;
import com.kmk.motatawera.doctor.model.QuizModel;

import java.util.List;

 public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

     List<QuizModel> quizModelList;
     Context context;
     String branch;
     OnQuizListener onQuizListener;

     public QuizAdapter(List<QuizModel> quizModelList, Context context, OnQuizListener onQuizListener) {
         this.quizModelList = quizModelList;
         this.context = context;
         this.onQuizListener = onQuizListener;
     }

     @NonNull
     @Override
     public QuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         QuizlistLayoutBinding quizlistLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                 R.layout.quizlist_layout, parent, false);
         return new QuizAdapter.ViewHolder(quizlistLayoutBinding);
     }

     @Override
     public void onBindViewHolder(@NonNull QuizAdapter.ViewHolder holder, int position) {


         if (quizModelList.get(position).getQuizActive()) {
             holder.quizlistLayoutBinding.btnStartquiz.setText("Close");
             holder.quizlistLayoutBinding.isactive.setImageResource(R.drawable.btn_active);

         } else {
             holder.quizlistLayoutBinding.btnStartquiz.setText("Start");
             holder.quizlistLayoutBinding.isactive.setImageResource(R.drawable.btn_notactive);
         }


         holder.quizlistLayoutBinding.btnStartquiz.setOnClickListener(v -> {
             onQuizListener.OnStartQuizListener(position, quizModelList);
             notifyDataSetChanged();
         });

         holder.quizlistLayoutBinding.btnDeleteQuiz.setOnClickListener(v -> {
             onQuizListener.OnDeleteQuizListener(position, quizModelList, context);
             notifyDataSetChanged();
         });
         holder.quizlistLayoutBinding.btnEditQuiz.setOnClickListener(v -> {
             onQuizListener.OnEditQuizListener(position, quizModelList);
             notifyDataSetChanged();
         });

         if (quizModelList.get(position).getSubject_branch() == 1)
             branch = "Haram";
         else
             branch = "Qtamia";


         Toast.makeText(context, quizModelList.get(position).getTitle() , Toast.LENGTH_SHORT).show();

         holder.quizlistLayoutBinding.txtQuiztitle.setText(quizModelList.get(position).getTitle());
         holder.quizlistLayoutBinding.txtBranch.setText(branch);
         holder.quizlistLayoutBinding.txtSubject.setText(quizModelList.get(position).getSubject_name());


     }

     @Override
     public int getItemCount() {
         return quizModelList.size();
     }


     public static class ViewHolder extends RecyclerView.ViewHolder {
         QuizlistLayoutBinding quizlistLayoutBinding;

         public ViewHolder(@NonNull QuizlistLayoutBinding quizlistLayoutBinding) {
             super(quizlistLayoutBinding.getRoot());
             this.quizlistLayoutBinding = quizlistLayoutBinding;
         }

     }

     public void setList(List<QuizModel> quizModels) {
         this.quizModelList = quizModels;
         notifyDataSetChanged();
     }

     public interface OnQuizListener {
         void OnStartQuizListener(int position, List<QuizModel> quizList);

         void OnEditQuizListener(int position, List<QuizModel> quizList);

         void OnDeleteQuizListener(int position, List<QuizModel> quizList, Context context);
     }
 }
