package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialogTheme))
                .setTitle(R.string.logout)
                .setMessage(R.string.confirm_logout_message)
                .setPositiveButton(R.string.positive_choice, (dialog, which) -> {
                    Toast.makeText(this, "✅ " + getResources().getString(R.string.confirm_logout_action), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(R.string.negative_choice, null)
                .show();
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