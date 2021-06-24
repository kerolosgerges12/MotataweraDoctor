package com.kmk.motatawera.doctor.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.adapter.QuestionsAdapter;
import com.kmk.motatawera.doctor.databinding.ActivityQuestionsListBinding;
import com.kmk.motatawera.doctor.model.QuestionsModel;

import java.util.ArrayList;
import java.util.List;


public class QuestionsListActivity extends AppCompatActivity implements QuestionsAdapter.OnQuestionListener {

    ActivityQuestionsListBinding binding;
    FirebaseFirestore db;
    List<QuestionsModel> questionsModelList;
    QuestionsAdapter questionsAdapter;
    String Title, QuizID, subject_name, branch;
    int subject_branch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_questions_list);
        db = FirebaseFirestore.getInstance();

        questionsModelList = new ArrayList<>();
        binding.rvQuestionsList.setHasFixedSize(true);
        binding.rvQuestionsList.setLayoutManager(new LinearLayoutManager(this));
        questionsAdapter = new QuestionsAdapter(questionsModelList, this, this);
        binding.rvQuestionsList.setAdapter(questionsAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);


        getSubjectdata();

        binding.txtBranch.setText(branch);
        binding.txtSubject.setText(subject_name);
        binding.btnDoneAddQuiz.setOnClickListener(v -> finish());

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle("Quiz : " + Title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.rvQuestionsList.setHasFixedSize(true);
        binding.rvQuestionsList.setLayoutManager(new LinearLayoutManager(this));


        binding.btnAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(QuestionsListActivity.this, AddQuestionsActivity.class)
                    .putExtra("QuizID", QuizID)
                    .putExtra("subject_name", subject_name)
                    .putExtra("subject_branch", subject_branch)
                    .putExtra("Title", Title);
            startActivity(intent);
        });

        binding.swp.setOnRefreshListener(() -> {
            binding.swp.setRefreshing(false);
        });


        GetQuestionsList();


    }


    private void getSubjectdata() {

        subject_name = getIntent().getStringExtra("subject_name");
        subject_branch = getIntent().getIntExtra("subject_branch", 0);
        Title = getIntent().getStringExtra("title");

        if (subject_branch == 1)
            branch = "Haram";
        else
            branch = "Qtamia";
    }


    private void GetQuestionsList() {

        QuizID = getIntent().getStringExtra("QuizID");
        db.collection("quiz").document(QuizID).collection("Questions")
                .addSnapshotListener((value, error) -> {
                    if (error == null) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (value == null) {
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {

                                binding.progressBar.setVisibility(View.GONE);

                                switch (documentChange.getType()) {

                                    case ADDED:
                                        onDocumentAdded(documentChange);
                                        break;
                                    case MODIFIED:
                                        onDocumentModified(documentChange);
                                        break;
                                    case REMOVED:
                                        onDocumentRemoved(documentChange);
                                        break;


                                }

                            }
                        }
                    } else {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    @Override
    public void onQuestionEditClick(int position, Context context, List<QuestionsModel> questionsList) {

        QuestionsModel Model = questionsList.get(position);
        String id = Model.getId();
        String Question = Model.getQuestion();
        String Answer1 = Model.getAnswer1();
        String Answer2 = Model.getAnswer2();
        String Answer3 = Model.getAnswer3();
        String Answer4 = Model.getAnswer4();
        String CorrectAnswer = Model.getCorrectAnswer();

        Intent intent = new Intent(context, EditQuestionActivity.class);
        intent.putExtra("QuizID", QuizID)
                .putExtra("QuestionID", id)
                .putExtra("Question", Question)
                .putExtra("Answer1", Answer1)
                .putExtra("Answer2", Answer2)
                .putExtra("Answer3", Answer3)
                .putExtra("Answer4", Answer4)
                .putExtra("CorrectAnswer", CorrectAnswer);
        startActivity(intent);

    }


    @Override
    public void onQuestionDeleteClick(int position, List<QuestionsModel> questionslList, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Question")
                .setMessage("Are you Sure you want to delete Question :" + questionslList.get(position).getQuestion())
                .setPositiveButton("Confirm", (dialog, id) -> {
                    db.collection("quiz")
                            .document(QuizID)
                            .collection("Questions")
                            .document(questionslList.get(position).getId()).delete();
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss())
                .show();
    }


    private void onDocumentAdded(DocumentChange change) {
        QuestionsModel model = change.getDocument().toObject(QuestionsModel.class);
        questionsModelList.add(model);
        questionsAdapter.notifyItemInserted(questionsModelList.size() - 1);
        questionsAdapter.getItemCount();

    }

    private void onDocumentModified(DocumentChange change) {
        try {
            QuestionsModel model = change.getDocument().toObject(QuestionsModel.class);
            if (change.getOldIndex() == change.getNewIndex()) {
                // Item changed but remained in same position
                questionsModelList.set(change.getOldIndex(), model);
                questionsAdapter.notifyItemChanged(change.getOldIndex());
            } else {
                // Item changed and changed position
                questionsModelList.remove(change.getOldIndex());
                questionsModelList.add(change.getNewIndex(), model);
                questionsAdapter.notifyItemRangeChanged(0, questionsModelList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDocumentRemoved(DocumentChange change) {
        try {
            questionsModelList.remove(change.getOldIndex());
            questionsAdapter.notifyItemRemoved(change.getOldIndex());
            questionsAdapter.notifyItemRangeChanged(0, questionsModelList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
