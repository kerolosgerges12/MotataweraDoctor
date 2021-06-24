package com.kmk.motatawera.doctor.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.ActivityLoginBinding;
import com.kmk.motatawera.doctor.model.DoctorModel;
import com.kmk.motatawera.doctor.storage.SharedPrefManager;
import com.kmk.motatawera.doctor.ui.MainActivity;
import com.kmk.motatawera.doctor.util.CheckInternetConn;

import static com.kmk.motatawera.doctor.util.Hide_Keyboard.hideKeyboard;
import static com.kmk.motatawera.doctor.util.ShowAlert.SHOW_ALERT;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (SharedPrefManager.getInstance().isLogin(this)) goToMain();


        binding.btnCreateNewAccount.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));

        binding.btnLogin.setOnClickListener(v -> {
            if (new CheckInternetConn(this).isConnection()) validationData();
            else SHOW_ALERT(this, "no internet connection");
        });

    }


    private void validationData() {

        String email = binding.etEmailLogin.getText().toString().trim();
        String password = binding.etPassLogin.getText().toString().trim();

        if (email.isEmpty()) {
            SHOW_ALERT(this, "pleas enter your Email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SHOW_ALERT(this, "Invalid email");
            return;
        }
        if (password.isEmpty()) {
            SHOW_ALERT(this, "Pleas enter your Password");
            return;
        }

        if (password.length() < 6) {
            SHOW_ALERT(this, "password must be to 6 char");
            return;
        }

        signIN(email, password);

    }

    private void signIN(String email, String password) {

        hideKeyboard(this);

        // run progress
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (firebaseAuth.getCurrentUser() != null) {

                            db.collection("doctor")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .get()
                                    .addOnCompleteListener(task1 -> {

                                        if (task1.isSuccessful()) {

                                            boolean isDelete = task1.getResult().getBoolean("deleted");
                                            boolean isDisable = task1.getResult().getBoolean("disable");
                                            boolean isApproved = task1.getResult().getBoolean("approved");

                                            if (!isDelete) {

                                                if (!isDisable) {

                                                    if (!isApproved) {
                                                        progressDialog.dismiss();
                                                        SHOW_ALERT(this, "account not Approved please wait");
                                                    } else {
                                                        if (task1.getResult().getString("email").equals(email) && task1.getResult().getString("password").equals(password)) {
                                                            SharedPrefManager.getInstance().saveUser(this, task1.getResult().toObject(DoctorModel.class));
                                                            goToMain();
                                                            progressDialog.dismiss();
                                                        } else {
                                                            Toast.makeText(this, "Incorrect Data", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                } else {
                                                    progressDialog.dismiss();
                                                    SHOW_ALERT(this, "account is Disable");
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                SHOW_ALERT(this, "account is deleted");
                                            }


                                        } else {
                                            progressDialog.dismiss();
                                            SHOW_ALERT(this, task.getException().getMessage());
                                        }

                                    });

                        } else {
                            progressDialog.dismiss();
                            SHOW_ALERT(this, task.getException().getMessage());
                        }
                    }
                });

    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
