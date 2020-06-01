package com.example.examenandroidgp.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.examenandroidgp.R;

public class Utils {

    public static void createAlertDialog(Context context, String message, DialogInterface.OnClickListener onClickListener){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface args) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
        });
        dialog.show();
    }
}
