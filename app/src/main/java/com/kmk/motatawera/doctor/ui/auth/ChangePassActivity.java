package com.kmk.motatawera.doctor.ui.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.databinding.ActivityChangePassBinding;
import com.kmk.motatawera.doctor.storage.SharedPrefManager;
import com.kmk.motatawera.doctor.util.CheckInternetConn;
import java.util.HashMap;
import java.util.Map;

import static com.kmk.motatawera.doctor.util.Hide_Keyboard.hideKeyboard;
import static com.kmk.motatawera.doctor.util.ShowAlert.SHOW_ALERT;

public class ChangePassActivity extends AppCompatActivity {

    private ActivityChangePassBinding binding;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pass);

        //setup toolbar
        setSupportActionBar(binding.toolbarChangPassword);
        binding.toolbarChangPassword.setNavigationOnClickListener(v -> finish());

        firestore = FirebaseFirestore.getInstance();

        binding.btnSave.setOnClickListener(v -> {
            if (new CheckInternetConn(this).isConnection()) {
                Toast.makeText(this, SharedPrefManager.getInstance().getUser(this).getPassword(), Toast.LENGTH_SHORT).show();
                checkValidation();
            } else SHOW_ALERT(this, "no internet");
        });

    }

    private void checkValidation() {


        String oldPass = binding.oldPassword.getText().toString().trim();
        String newPass = binding.newPassword.getText().toString().trim();
        String cNewPass = binding.confirmNewPassword.getText().toString().trim();

        if (oldPass.isEmpty()) {
            binding.oldPassword.requestFocus();
            SHOW_ALERT(this, "pleas enter your old password!");
            return;
        }
        if (newPass.isEmpty()) {
            binding.newPassword.requestFocus();
            SHOW_ALERT(this, "Pleas enter your password!");
            return;
        }
        if (cNewPass.isEmpty()) {
            binding.confirmNewPassword.requestFocus();
            SHOW_ALERT(this, "Pleas enter your confirm password!");
            return;
        }

        if (newPass.length() < 6 & cNewPass.length() < 6) {
            SHOW_ALERT(this, "all password should be greater than 6 char!");
            return;
        }

        if (!newPass.equals(cNewPass)) {
            SHOW_ALERT(this, "new password should be equal confirm new password!");
            return;
        }

        if (!oldPass.equals(SharedPrefManager.getInstance().getUser(this).getPassword())) {
            SHOW_ALERT(this, "old password is incorrect!");
            return;
        }

        hideKeyboard(this);

        updatePassword();

    }

    private void updatePassword() {

        // mack progress bar dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("password", binding.newPassword.getText().toString().trim());


        firestore.collection("doctor")
                .document(SharedPrefManager.getInstance().getUser(this).getId())
                .update(map)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();

                        new AlertDialog.Builder(this)
                                .setMessage("password update successfully!")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, which) -> {

                                    SharedPrefManager.getInstance().updatePassword(this, binding.newPassword.getText().toString().trim());

                                    finish();
                                })
                                .create()
                                .show();

                    } else {

                        SHOW_ALERT(this, task.getException().getMessage());
                        progressDialog.dismiss();

                    }

                });

    }
}