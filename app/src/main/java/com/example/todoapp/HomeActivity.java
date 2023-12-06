package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.todoapp.Adapters.ToDoAdapter;
import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.Utils.MyDatabase;
import com.example.todoapp.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnDialogCloseListener {

    private List<ToDoModel> todos_list;
    private ToDoAdapter adapter;
    private MyDatabase db;

    private int user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        user_id = getIntent().getIntExtra("user_id", 0);

        setContentView(binding.getRoot());

        RecyclerView recyclerView = binding.todoRecyclerView;
        FloatingActionButton fab = binding.addTodoButton;
        db = new MyDatabase(HomeActivity.this);
        todos_list = new ArrayList<>();
        adapter = new ToDoAdapter(db, HomeActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        todos_list = db.getAllTasks(user_id);
        Collections.reverse(todos_list);
        adapter.setTasks(todos_list);

        fab.setOnClickListener(v -> onAddTodoButtonClicked());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

            if(item.getItemId() == R.id.logout)
            {
                alertLogoutDialog();
            }

            return super.onOptionsItemSelected(item);
    }

    private void alertLogoutDialog() {
        String title = getResources().getString(R.string.logout);
        String message = getResources().getString(R.string.confirm_logout_message);

        ConfirmDialog confirmDialog = new ConfirmDialog(this, title, message);
        confirmDialog.setOnPositiveButtonClickListener(() -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        confirmDialog.setOnNegativeButtonClickListener(null);
        String toastMessage = "✅ " + getResources().getString(R.string.confirm_logout_action);
        confirmDialog.startConfirmDialog(toastMessage);
    }

    private void onAddTodoButtonClicked() {
        String addTaskHint = getResources().getString(R.string.add_task_hint);
        String buttonText = getResources().getString(R.string.add_task_button_text);
        AddAndUpdateTask.newInstance(addTaskHint, buttonText).show(getSupportFragmentManager(), AddAndUpdateTask.TAG);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface)
    {
        todos_list = db.getAllTasks(user_id);
        Collections.reverse(todos_list);
        adapter.setTasks(todos_list);
        adapter.notifyDataSetChanged();
    }
}