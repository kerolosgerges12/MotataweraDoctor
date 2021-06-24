package com.kmk.motatawera.doctor.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.kmk.motatawera.doctor.model.DoctorModel;

import static com.motatawera.doctor.Const.SHARED_PREF_DOCTOR;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "userShared";
    private static SharedPrefManager mInstance;


    public static synchronized SharedPrefManager getInstance() {
        if (mInstance == null)
            mInstance = new SharedPrefManager();
        return mInstance;
    }

    public void saveUser(Context context, DoctorModel userModel) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", userModel.getId());
        editor.putString("name", userModel.getName());
        editor.putString("email", userModel.getEmail());
        editor.putString("password", userModel.getPassword());
        editor.putInt("branch", userModel.getBranch());
        editor.putBoolean("isDoctor", userModel.isDoctor());
        editor.putBoolean("isApproved", userModel.isApproved());
        editor.putBoolean("isDisable", userModel.isDisable());
        editor.putBoolean("isDelete", userModel.isDeleted());
        editor.apply();
    }

    public boolean isLogin(Context context) {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString("id", null) != null;
    }

    public DoctorModel getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new DoctorModel(
                preferences.getString("id", null),
                preferences.getString("name", null),
                preferences.getString("email", null),
                preferences.getString("password", null),
                preferences.getInt("branch",-1),
                preferences.getBoolean("isDoctor", false),
                preferences.getBoolean("isApproved", false),
                preferences.getBoolean("isDisable", false),
                preferences.getBoolean("isDelete", false)
        );

    }

    public void updatePassword(Context context, String newPassword) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_DOCTOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", newPassword);
        editor.apply();
    }
    public void logOut(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}