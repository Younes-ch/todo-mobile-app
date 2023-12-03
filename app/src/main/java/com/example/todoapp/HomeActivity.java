package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.todoapp.Adapters.ToDoAdapter;
import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.Utils.MyDatabase;
import com.example.todoapp.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnDialogCloseListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ActivityHomeBinding binding;

    private List<ToDoModel> todos_list;
    private ToDoAdapter adapter;
    private MyDatabase db;

    private int user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_id = getIntent().getIntExtra("user_id", 0);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.todoRecyclerView;
        fab = binding.addTodoButton;
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

    }

    private void onAddTodoButtonClicked() {
        AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
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