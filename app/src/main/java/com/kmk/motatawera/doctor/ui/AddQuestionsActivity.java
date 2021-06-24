package com.kmk.motatawera.doctor.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.ActivityAddQuestionsBinding;
import com.kmk.motatawera.doctor.model.QuestionsModel;

public class AddQuestionsActivity extends AppCompatActivity {

    ActivityAddQuestionsBinding binding;

    FirebaseFirestore db;
    int subject_branch;
    String QuizID, Question, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer, subject_name, Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_questions);
        db = FirebaseFirestore.getInstance();
        QuizID = getIntent().getStringExtra("QuizID");
        subject_name = getIntent().getStringExtra("subject_name");
        subject_branch = getIntent().getIntExtra("subject_branch", 0);
        Title = getIntent().getStringExtra("title");

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle("Add Questions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        binding.addQuestionDialogBtnDone.setOnClickListener(v -> finish()
        );


        binding.btnAddQuestion.setOnClickListener(v -> {
            val();
        });

    }

    private void val() {
        Question = binding.addQuestionDialogTxtAddQuestionTitle.getText().toString();
        AnswerA = binding.addQuestionDialogTxtAddAnswer1.getText().toString();
        AnswerB = binding.addQuestionDialogTxtAddAnswer2.getText().toString();
        AnswerC = binding.addQuestionDialogTxtAddAnswer3.getText().toString();
        AnswerD = binding.addQuestionDialogTxtAddAnswer4.getText().toString();


        if (Question.isEmpty()) {
            binding.addQuestionDialogTxtAddQuestionTitle.setError("required Question");

        } else if (AnswerA.isEmpty()) {
            binding.addQuestionDialogTxtAddAnswer1.setError("required Answer 1");
        } else if (AnswerB.isEmpty()) {
            binding.addQuestionDialogTxtAddAnswer2.setError("required Answer 2");
        } else if (AnswerC.isEmpty()) {
            binding.addQuestionDialogTxtAddAnswer3.setError("required Answer 3");
        } else if (AnswerD.isEmpty()) {
            binding.addQuestionDialogTxtAddAnswer4.setError("required Answer 4");
        } else {
            if (binding.addQuestionDialogRbAnswer1.isChecked()) {
                CorrectAnswer = AnswerA;
            }
            if (binding.addQuestionDialogRbAnswer2.isChecked()) {
                CorrectAnswer = AnswerB;
            }
            if (binding.addQuestionDialogRbAnswer3.isChecked()) {
                CorrectAnswer = AnswerC;
            }

            if (binding.addQuestionDialogRbAnswer4.isChecked()) {
                CorrectAnswer = AnswerD;
            }

            AddQuestionToQuiz(Question, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer);
        }
    }


    private void AddQuestionToQuiz(String Question, String AnswerA, String AnswerB, String AnswerC, String AnswerD, String CorrectAnswer) {


        DocumentReference documentReference;
        documentReference = db.collection("quiz")
                .document(QuizID).collection("Questions").document();

        QuestionsModel model = new QuestionsModel();
        model.setId(documentReference.getId());
        model.setQuestion(Question);
        model.setAnswer1(AnswerA);
        model.setAnswer2(AnswerB);
        model.setAnswer3(AnswerC);
        model.setAnswer4(AnswerD);
        model.setCorrectAnswer(CorrectAnswer);
        documentReference.set(model).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String QuestionID = model.getId();
                db.collection("quiz")
                        .document(QuizID)
                        .collection("Questions")
                        .document(QuestionID)
                        .update("id", QuestionID);
                Toast.makeText(this, "Question Added Successfully", Toast.LENGTH_SHORT).show();
                ClearText();
            }


        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void ClearText() {
        binding.addQuestionDialogTxtAddQuestionTitle.getText().clear();
        binding.addQuestionDialogTxtAddAnswer1.getText().clear();
        binding.addQuestionDialogTxtAddAnswer2.getText().clear();
        binding.addQuestionDialogTxtAddAnswer3.getText().clear();
        binding.addQuestionDialogTxtAddAnswer4.getText().clear();
    }

}