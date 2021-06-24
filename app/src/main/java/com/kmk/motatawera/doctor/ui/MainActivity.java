package com.kmk.motatawera.doctor.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.messaging.FirebaseMessaging;

import com.kmk.motatawera.doctor.R;
import com.kmk.motatawera.doctor.storage.SharedPrefManager;
import com.kmk.motatawera.doctor.util.CheckInternetConn;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        initNav();
    }

    private void initNav() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        AppBarConfiguration configuration = new AppBarConfiguration.Builder(
                R.id.nav_quiz,
                R.id.nav_notification,
                R.id.nav_profile
        ).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigation, navController);

        NavigationUI.setupActionBarWithNavController(this, navController, configuration);

        navigation.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.nav_quiz:
                    navController.navigate(R.id.nav_quiz);
                    return true;

                case R.id.nav_notification:
                    navController.navigate(R.id.nav_notification);
                    return true;

                case R.id.nav_profile:
                    navController.navigate(R.id.nav_profile);
                    return true;
            }

            return false;
        });

    }

    @Override
    public void onBackPressed() {

        if (navController.getGraph().getStartDestination() == R.id.nav_quiz) finish();
        else super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateToken();
    }

    private void updateToken() {
        String uid = SharedPrefManager.getInstance().getUser(this).getId();
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