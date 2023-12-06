package com.example.todoapp;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmDialog extends Dialog
{
    private OnPositiveButtonClickListener onPositiveButtonClickListener;
    private OnNegativeButtonClickListener onNegativeButtonClickListener;

    private final String title;
    private final String message;

    ConfirmDialog(Context context, String title, String message) {
        super(context);
        this.title = title;
        this.message = message;
    }

    public void startConfirmDialog(String toastMessage)
    {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setWindowAnimations(R.style.windowAnimation);
        setContentView(R.layout.custom_alert_dialog);
        setCancelable(false);
        TextView titleTextView = (TextView) findViewById(R.id.customTitle);
        titleTextView.setText(title);
        TextView messageTextView = (TextView) findViewById(R.id.customMessage);
        messageTextView.setText(message);
        Button confirmButton = (Button) findViewById(R.id.customPositiveButton);
        confirmButton.setOnClickListener(v -> {
            if(onPositiveButtonClickListener != null)
            {
                onPositiveButtonClickListener.onPositiveButtonClick();
            }
            dismissDialog(toastMessage);
        });
        Button cancelButton = (Button) findViewById(R.id.customNegativeButton);
        cancelButton.setOnClickListener(v -> {
            if(onNegativeButtonClickListener != null)
            {
                onNegativeButtonClickListener.onNegativeButtonClick();
            }
            dismissDialog();
        });
        this.show();
    }

    public void dismissDialog()
    {
        this.dismiss();
    }
    public void dismissDialog(String toastMessage)
    {
        this.dismiss();
        Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener) {
        this.onPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    public void setOnNegativeButtonClickListener(OnNegativeButtonClickListener onNegativeButtonClickListener) {
        this.onNegativeButtonClickListener = onNegativeButtonClickListener;
    }

}

