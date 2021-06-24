package com.kmk.motatawera.doctor.ui.auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.messaging.FirebaseMessaging;

import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.ActivityRegistrationBinding;
import com.kmk.motatawera.doctor.model.DoctorModel;
import com.kmk.motatawera.doctor.storage.SharedPrefManager;
import com.kmk.motatawera.doctor.util.CheckInternetConn;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.kmk.motatawera.doctor.util.Hide_Keyboard.hideKeyboard;
import static com.kmk.motatawera.doctor.util.ShowAlert.SHOW_ALERT;


public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String device_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.btnRegister.setOnClickListener(v -> {
            if (new CheckInternetConn(this).isConnection()) validation();
            else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });


    }

    private void validation() {

        String fullName = binding.etNameRegister.getText().toString();
        String email = binding.etEmailRegister.getText().toString();
        String password = binding.etPassRegister.getText().toString();

        if (fullName.isEmpty()) {
            SHOW_ALERT(this, "pleas enter your Name");
            return;
        }
        if (email.isEmpty()) {
            SHOW_ALERT(this, "pleas enter your email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SHOW_ALERT(this, "Invalid email");
            return;
        }
        if (password.isEmpty()) {
            SHOW_ALERT(this, "Pleas enter your password");
            return;

        }
        if (password.length() < 6) {
            SHOW_ALERT(this, "password must be to 6 char");
            return;
        }

        hideKeyboard(this);

        createNewUser(fullName, email, password);

    }


    private void createNewUser(String Name, String Email, String Password) {

        // run progress
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        if (firebaseAuth.getCurrentUser() != null) {

                            String uid = firebaseAuth.getCurrentUser().getUid();
                            DoctorModel doctorModel = new DoctorModel();

                            doctorModel.setId(uid) ;
                            doctorModel.setName(Name);
                            doctorModel.setEmail(Email);
                            doctorModel.setPassword(Password);
                            doctorModel.setApproved(false);
                            doctorModel.setDeleted(false);
                            doctorModel.setDisable(false);


                            if (binding.cbHaram.isChecked() && binding.cbQtamia.isChecked())
                                doctorModel.setBranch(3);
                            else if (binding.cbHaram.isChecked())
                                doctorModel.setBranch(1);
                            else if (binding.cbQtamia.isChecked())
                                doctorModel.setBranch(2);


                            if (binding.rbDoctor.isChecked())
                                doctorModel.setDoctor(true);
                            else
                                doctorModel.setDoctor(true);

                            db.collection("doctor")
                                    .document(uid)
                                    .set(doctorModel)
                                    .addOnCompleteListener(value -> {
                                        if (value.isSuccessful()) {

                                            progressDialog.dismiss();

                                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(RegistrationActivity.this, "Account Created Successful", Toast.LENGTH_SHORT).show();
                                            updateToken( uid);

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, value.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            finish();
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateToken(String uid) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        String token = task.getResult();
                        FirebaseFirestore.getInstance().collection("doctor").document(uid).get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        DocumentReference db = FirebaseFirestore.getInstance()
                                                .collection("doctor")
                                                .document(uid);
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("token", token);
                                        db.update(map);
                                    }
                                });
                    }
                });
    }
}