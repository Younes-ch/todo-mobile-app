package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.Utils.Connectivity;
import com.example.todoapp.Utils.JavaMailAPI;
import com.example.todoapp.Utils.MyDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Random;

public class AddAndUpdateTask extends BottomSheetDialogFragment
{
    public static final String TAG = "AddAndUpdateTask";

    private final String editTextHint;
    private final String buttonText;
    private EditText editText;
    private Button button;

    private MyDatabase db;

    public AddAndUpdateTask(String editTextHint, String buttonText) {
        this.editTextHint = editTextHint;
        this.buttonText = buttonText;
    }
    public static AddAndUpdateTask newInstance(String editTextHint, String buttonText) {
        return new AddAndUpdateTask(editTextHint, buttonText);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_update_task, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.bottomDialogEditText);
        editText.setHint(editTextHint);
        button = view.findViewById(R.id.bottomDialogButton);
        button.setText(buttonText);
        button.setEnabled(false);

        db = new MyDatabase(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            isUpdate = true;
            String title = bundle.getString("title");
            editText.setText(title);
            if (title.length() >= 0)
            {
                button.setEnabled(false);
            }
        }


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    button.setEnabled(false);
                    button.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else
                {
                    button.setEnabled(true);
                    button.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        button.setOnClickListener(v -> {
            String text = editText.getText().toString().trim();
            if(finalIsUpdate)
            {
                db.updateTaskTitle(bundle.getInt("id"), text);

                if (Connectivity.isNetworkConnected(getActivity()))
                {
                    int userId = getActivity().getIntent().getIntExtra("user_id", 0);
                    String email = db.getUserEmail(userId);
                    String subject = getResources().getString(R.string.task_updated_email_subject);
                    String body = getResources().getString(R.string.task_updated_email_body) + ": \n- Before: "
                            + bundle.getString("title") + "\n- After: " + text;
                    JavaMailAPI.sendMail(getActivity(), email, subject, body);
                } else {
                    Connectivity.showNoInternetMessage(getActivity());
                }
                String toastMessage = getResources().getString(R.string.task_updated_email_subject);
                Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
            }
            else
            {
                int userId = getActivity().getIntent().getIntExtra("user_id", 0);
                ToDoModel task = new ToDoModel(new Random().nextInt(100), text, 0,
                        userId);
                db.insertTask(task);

                if (Connectivity.isNetworkConnected(getActivity()))
                {
                    String email = db.getUserEmail(userId);
                    String subject = "✅ " + getResources().getString(R.string.new_task_email_subject);
                    String body = getResources().getString(R.string.new_task_email_body) + ": \n- " + text;
                    JavaMailAPI.sendMail(getActivity(), email, subject, body);
                } else {
                    Connectivity.showNoInternetMessage(getActivity());
                }
                String toastMessage = "✅ " + getResources().getString(R.string.new_task_email_subject);
                Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListener)
        {
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }

    }
}
