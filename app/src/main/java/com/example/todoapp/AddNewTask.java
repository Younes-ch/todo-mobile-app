package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.Utils.MyDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Contract;

import java.util.Random;

public class AddNewTask extends BottomSheetDialogFragment
{
    public static final String TAG = "AddNewTask";

    private EditText newTaskText;
    private Button newTaskAddButton;

    private MyDatabase db;
    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newTaskText = view.findViewById(R.id.taskEditText);
        newTaskAddButton = view.findViewById(R.id.addTaskButton);

        db = new MyDatabase(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            isUpdate = true;
            String title = bundle.getString("title");
            newTaskText.setText(title);
            if (title.length() > 0)
            {
                newTaskAddButton.setEnabled(false);
            }
        }

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    newTaskAddButton.setEnabled(false);
                    newTaskAddButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    newTaskAddButton.setTextColor(getResources().getColor(R.color.white));
                }
                else
                {
                    newTaskAddButton.setEnabled(true);
                    newTaskAddButton.setBackgroundColor(getResources().getColor(R.color.dark_blue));
                    newTaskAddButton.setTextColor(getResources().getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskAddButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString().trim();
            if(finalIsUpdate)
            {
                db.updateTaskTitle(bundle.getInt("id"), text);
            }
            else
            {
                int userId = getActivity().getIntent().getIntExtra("user_id", 0);
                ToDoModel task = new ToDoModel(new Random().nextInt(100), text, 0,
                        userId);
                db.insertTask(task);
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
