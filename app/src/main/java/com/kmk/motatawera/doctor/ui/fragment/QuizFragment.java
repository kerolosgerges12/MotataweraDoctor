package com.kmk.motatawera.doctor.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.adapter.QuizAdapter;
import com.kmk.motatawera.doctor.databinding.FragmentQuizBinding;
import com.kmk.motatawera.doctor.model.QuizModel;
import com.kmk.motatawera.doctor.storage.SharedPrefManager;
import com.kmk.motatawera.doctor.ui.MainActivity;
import com.kmk.motatawera.doctor.ui.QuestionsListActivity;
import com.kmk.motatawera.doctor.ui.SubjectsActivity;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class QuizFragment extends Fragment implements QuizAdapter.OnQuizListener {

    private FragmentQuizBinding binding;
    private FirebaseFirestore db;
    List<QuizModel> quizModelList;
    QuizAdapter quizAdapter;
    private static final String TAG = "QuizFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());

                    } else {
                        String token = task.getResult();
                        Log.d(TAG, "onComplete: token" + token);
                        Toast.makeText(getActivity(), token, Toast.LENGTH_SHORT).show();
                    }
                });

        binding.rvQuizlist.setHasFixedSize(true);
        binding.rvQuizlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        quizModelList = new ArrayList<>();
        quizAdapter = new QuizAdapter(quizModelList, getActivity(), this);


        binding.progressBar.setVisibility(View.VISIBLE);

        getQuizList();

        binding.btnFabAddquiz.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SubjectsActivity.class));
        });

    }

    private void getQuizList() {
        String id = SharedPrefManager.getInstance().getUser(getActivity()).getId();
        db.collection("quiz")
                .whereEqualTo("doctor_id", id)
                .addSnapshotListener((value, error) -> {
                    if (error == null) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (value == null) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentChange documentChange : value.getDocumentChanges()) {

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

                                binding.progressBar.setVisibility(View.GONE);
                                binding.rvQuizlist.setAdapter(quizAdapter);
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void onDocumentAdded(DocumentChange documentChange) {
        QuizModel model = documentChange.getDocument().toObject(QuizModel.class);
        quizModelList.add(model);
        quizAdapter.notifyItemInserted(quizModelList.size() - 1);
        quizAdapter.getItemCount();
    }

    private void onDocumentModified(DocumentChange documentChange) {
        try {
            QuizModel model = documentChange.getDocument().toObject(QuizModel.class);
            if (documentChange.getOldIndex() == documentChange.getNewIndex()) {
                // Item changed but remained in same position
                quizModelList.set(documentChange.getOldIndex(), model);
                quizAdapter.notifyItemChanged(documentChange.getOldIndex());
            } else {
                // Item changed and changed position
                quizModelList.remove(documentChange.getOldIndex());
                quizModelList.add(documentChange.getNewIndex(), model);
                quizAdapter.notifyItemRangeChanged(0, quizModelList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onDocumentRemoved(DocumentChange documentChange) {
        try {
            quizModelList.remove(documentChange.getOldIndex());
            quizAdapter.notifyItemRemoved(documentChange.getOldIndex());
            quizAdapter.notifyItemRangeChanged(0, quizModelList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnStartQuizListener(int position, List<QuizModel> quizList) {

        if (quizList.get(position).getQuizActive()) {
            quizList.get(position).setQuizActive(false);
        } else {
            quizList.get(position).setQuizActive(true);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("quizActive", quizList.get(position).getQuizActive());

        db.collection("quiz")
                .document(quizList.get(position).getId())
                .update("quizActive", quizList.get(position).getQuizActive())
                .addOnSuccessListener(aVoid -> {
                    if (quizList.get(position).getQuizActive()) {
                        Toast.makeText(getActivity(), "Quiz Started", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Quiz Stopped", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void OnEditQuizListener(int position, List<QuizModel> quizList) {
        Intent intent = new Intent(getActivity(), QuestionsListActivity.class)
                .putExtra("QuizID", quizList.get(position).getId())
                .putExtra("subject_name", quizList.get(position).getSubject_name())
                .putExtra("title", quizList.get(position).getTitle())
                .putExtra("subject_branch", quizList.get(position).getSubject_branch());
        startActivity(intent);
    }

    @Override
    public void OnDeleteQuizListener(int position, List<QuizModel> list, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Quiz")
                .setMessage("Are you Sure you want to delete Quiz :" + list.get(position).getTitle())
                .setPositiveButton("Confirm", (dialog, id) -> {
                    Toast.makeText(context, "Quiz Deleted Successfully", Toast.LENGTH_SHORT).show();
                    db.collection("quiz").document(list.get(position).getId()).delete();
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss())
                .show();
    }


}