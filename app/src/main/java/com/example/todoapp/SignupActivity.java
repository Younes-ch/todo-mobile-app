package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.todoapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.usernameInput.setOnClickListener(v -> onUsernameFieldClicked());
    }

    private void onUsernameFieldClicked() {
        // change the stroke color of the username field to light blue

    }

}