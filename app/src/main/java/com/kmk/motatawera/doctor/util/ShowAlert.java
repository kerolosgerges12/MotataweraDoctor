package com.kmk.motatawera.doctor.util;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by hosnyDev on 1/10/2021
 */
public class ShowAlert {

    public static void SHOW_ALERT(Context context, String msg) {
        if (context != null)
            new AlertDialog.Builder(context)
                    .setMessage(msg)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
    }
}
