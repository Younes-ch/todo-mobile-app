package com.example.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class LoadingDialog extends Dialog
{
    private final Context context;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void startLoadingDialog()
    {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        getWindow().setWindowAnimations(R.style.windowAnimation);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
        setContentView(view);
        setCancelable(false);
        this.show();
    }

    public void dismissDialog()
    {
        this.cancel();
    }
}
