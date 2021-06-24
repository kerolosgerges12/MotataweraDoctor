package com.kmk.motatawera.doctor.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.adapter.SubjectAdapter;
import com.kmk.motatawera.doctor.databinding.ActivitySubjectsBinding;
import com.kmk.motatawera.doctor.model.DoctorModel;
import com.kmk.motatawera.doctor.model.SubjectModel;
import com.kmk.motatawera.doctor.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;


public class SubjectsActivity extends AppCompatActivity {

    //view
    private ActivitySubjectsBinding binding;
    private SubjectAdapter subjectAdapter;
    private FirebaseFirestore db;
    private List<SubjectModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subjects);

        db = FirebaseFirestore.getInstance();
        binding.progressBar.setVisibility(View.VISIBLE);

        binding.rvSubjects.setHasFixedSize(true);
        binding.rvSubjects.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(this, list);

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle("Subjects");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        getSubjectsData();


        binding.swp.setOnRefreshListener(() -> {
            binding.swp.setRefreshing(false);
            getSubjectsData();
        });


    }

    private void getSubjectsData() {

        DoctorModel doctorModel = SharedPrefManager.getInstance().getUser(this);

        db.collection("subject").whereEqualTo("doctor_id", doctorModel.getId())
                .addSnapshotListener((value, error) -> {
                    if (error == null) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (value == null) {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                binding.rvSubjects.setAdapter(subjectAdapter);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });

    }


    private void onDocumentAdded(DocumentChange change) {
        SubjectModel model = change.getDocument().toObject(SubjectModel.class);
        list.add(model);
        subjectAdapter.notifyItemInserted(list.size() - 1);
        subjectAdapter.getItemCount();

    }

    private void onDocumentModified(DocumentChange change) {
        try {
            SubjectModel model = change.getDocument().toObject(SubjectModel.class);
            if (change.getOldIndex() == change.getNewIndex()) {
                // Item changed but remained in same position
                list.set(change.getOldIndex(), model);
                subjectAdapter.notifyItemChanged(change.getOldIndex());
            } else {
                // Item changed and changed position
                list.remove(change.getOldIndex());
                list.add(change.getNewIndex(), model);
                subjectAdapter.notifyItemRangeChanged(0, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDocumentRemoved(DocumentChange change) {
        try {
            list.remove(change.getOldIndex());
            subjectAdapter.notifyItemRemoved(change.getOldIndex());
            subjectAdapter.notifyItemRangeChanged(0, list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}