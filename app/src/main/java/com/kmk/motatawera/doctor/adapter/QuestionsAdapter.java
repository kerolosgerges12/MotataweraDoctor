package com.kmk.motatawera.doctor.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.QuestionLayoutBinding;
import com.kmk.motatawera.doctor.model.QuestionsModel;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    List<QuestionsModel> questionsModelList;
    Context context;
    OnQuestionListener onQuestionListener;

    public QuestionsAdapter(List<QuestionsModel> questionsModelList, Context context, OnQuestionListener onQuestionListener) {
        this.questionsModelList = questionsModelList;
        this.context = context;
        this.onQuestionListener = onQuestionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionLayoutBinding questionLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.question_layout, parent, false);
        return new ViewHolder(questionLayoutBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.questionLayoutBinding.txtQuestiontitle.setText(questionsModelList.get(position).getQuestion());
        holder.questionLayoutBinding.rbA.setText(questionsModelList.get(position).getAnswer1());
        holder.questionLayoutBinding.rbB.setText(questionsModelList.get(position).getAnswer2());
        holder.questionLayoutBinding.rbC.setText(questionsModelList.get(position).getAnswer3());
        holder.questionLayoutBinding.rbD.setText(questionsModelList.get(position).getAnswer4());
        String word = "Correct Answer is :"+questionsModelList.get(position).getCorrectAnswer();
        holder.questionLayoutBinding.txtCorrectAnswerAdapter.setText(word);


        if (questionsModelList.get(position).getAnswer1().equals(questionsModelList.get(position).getCorrectAnswer())) {
            holder.questionLayoutBinding.rbA.setChecked(true);
        }
        if (questionsModelList.get(position).getAnswer2().equals(questionsModelList.get(position).getCorrectAnswer())) {
            holder.questionLayoutBinding.rbB.setChecked(true);
        }
        if (questionsModelList.get(position).getAnswer3().equals(questionsModelList.get(position).getCorrectAnswer())) {
            holder.questionLayoutBinding.rbC.setChecked(true);
        }
        if (questionsModelList.get(position).getAnswer4().equals(questionsModelList.get(position).getCorrectAnswer())) {
            holder.questionLayoutBinding.rbD.setChecked(true);
        }


        holder.questionLayoutBinding.btnDelete.setOnClickListener(v -> {
            onQuestionListener.onQuestionDeleteClick(position, questionsModelList, context);
        });

        holder.questionLayoutBinding.btnEdit.setOnClickListener(v -> {
            onQuestionListener.onQuestionEditClick(position, context, questionsModelList);
        });
    }

    public void setList(List<QuestionsModel> questionsModelList) {
        this.questionsModelList = questionsModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return questionsModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        QuestionLayoutBinding questionLayoutBinding;

        public ViewHolder(@NonNull QuestionLayoutBinding questionLayoutBinding) {
            super(questionLayoutBinding.getRoot());
            this.questionLayoutBinding = questionLayoutBinding;
        }


    }

    public interface OnQuestionListener {


        void onQuestionEditClick(int position, Context context, List<QuestionsModel> questionsModelList);

        void onQuestionDeleteClick(int position, List<QuestionsModel> questionsModelList, Context context);
    }


}
