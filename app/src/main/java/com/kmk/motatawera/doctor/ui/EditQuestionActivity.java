package com.kmk.motatawera.doctor.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.ActivityEditQuestionBinding;

import java.util.HashMap;
import java.util.Map;

public class EditQuestionActivity extends AppCompatActivity {

    String QuizID, id, Question, Answer1, Answer2, Answer3, Answer4, CorrectAnswer, NewCorrectAnswer;
    Map<String, Object> QuestionUpdate;
    ActivityEditQuestionBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_question);
        db = FirebaseFirestore.getInstance();

        GetQuestionInfo();
        SetOldData();

        //Cancle Btn
        binding.editQuestionDiaBtnCancle.setOnClickListener(v -> finish());

        //Done Btn
        binding.editQuestionDialogBtnDone.setOnClickListener(v -> {
            SetNewData();
        });

    }

    private void SetOldData() {

        EditText et_editQuestion, et_editAnswer1, et_editAnswer2, et_editAnswer3, et_editAnswer4;

        et_editQuestion = binding.editQuestionDialogTxtEditQuestionTitle;
        et_editAnswer1 = binding.editQuestionDialogTxtAddAnswer1;
        et_editAnswer2 = binding.editQuestionDialogTxtAddAnswer2;
        et_editAnswer3 = binding.editQuestionDialogTxtAddAnswer3;
        et_editAnswer4 = binding.editQuestionDialogTxtAddAnswer4;

        et_editQuestion.setText(Question);
        et_editAnswer1.setText(Answer1);
        et_editAnswer2.setText(Answer2);
        et_editAnswer3.setText(Answer3);
        et_editAnswer4.setText(Answer4);


        //get previous Correct Answer Checked
        if (Answer1.equals(CorrectAnswer)) {
            binding.editQuestionDialogRbAnswer1.setChecked(true);
        }
        if (Answer2.equals(CorrectAnswer)) {
            binding.editQuestionDialogRbAnswer2.setChecked(true);
        }
        if (Answer3.equals(CorrectAnswer)) {

            binding.editQuestionDialogRbAnswer3.setChecked(true);
        }
        if (Answer4.equals(CorrectAnswer)) {
            binding.editQuestionDialogRbAnswer4.setChecked(true);
        }

    }

    private void SetNewData() {
        //Set New Data

        if (binding.editQuestionDialogRbAnswer1.isChecked()) {
            NewCorrectAnswer = binding.editQuestionDialogTxtAddAnswer1.getText().toString();
        } else if (binding.editQuestionDialogRbAnswer2.isChecked()) {
            NewCorrectAnswer = binding.editQuestionDialogTxtAddAnswer2.getText().toString();
        } else if (binding.editQuestionDialogRbAnswer3.isChecked()) {
            NewCorrectAnswer = binding.editQuestionDialogTxtAddAnswer3.getText().toString();
        } else if (binding.editQuestionDialogRbAnswer4.isChecked()) {
            NewCorrectAnswer = binding.editQuestionDialogTxtAddAnswer4.getText().toString();
        }

        map();
    }


    private void map() {

        QuestionUpdate = new HashMap<>();
        QuestionUpdate.put("question", binding.editQuestionDialogTxtEditQuestionTitle.getText().toString());
        QuestionUpdate.put("answer1", binding.editQuestionDialogTxtAddAnswer1.getText().toString());
        QuestionUpdate.put("answer2", binding.editQuestionDialogTxtAddAnswer2.getText().toString());
        QuestionUpdate.put("answer3", binding.editQuestionDialogTxtAddAnswer3.getText().toString());
        QuestionUpdate.put("answer4", binding.editQuestionDialogTxtAddAnswer4.getText().toString());
        QuestionUpdate.put("correctAnswer", NewCorrectAnswer);
        EditQuestion();
        finish();
    }

    private void EditQuestion() {
        db.collection("quiz")
                .document(QuizID)
                .collection("Questions")
                .document(id)
                .update(QuestionUpdate)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Question Updated Successfully", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show());

    }

    private void GetQuestionInfo() {

        QuizID = getIntent().getStringExtra("QuizID");
        id = getIntent().getStringExtra("QuestionID");
        Question = getIntent().getStringExtra("Question");
        Answer1 = getIntent().getStringExtra("Answer1");
        Answer2 = getIntent().getStringExtra("Answer2");
        Answer3 = getIntent().getStringExtra("Answer3");
        Answer4 = getIntent().getStringExtra("Answer4");
        CorrectAnswer = getIntent().getStringExtra("CorrectAnswer");

    }

}